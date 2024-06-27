import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import lombok.extern.slf4j.Slf4j;
import tool.model.DocMapper;
import tool.model.TestCount;
import tool.model.TestData;
import tool.model.TestDoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.MarshalException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ExcelGenerator {
    public static final String SET_UP = "setUp";
    static Pattern pattern1 = Pattern.compile("(目的|期望结果)[:：]\\s{0,}(.*)");
    static Pattern pattern2 = Pattern.compile("//[\\s]{0,}data([\\s\\S]{1,})//[\\s]{0,}stub");
    AtomicInteger assertCounter = new AtomicInteger(0);

    public int getAssertCounter() {
        return assertCounter.get();
    }

    public List<TestDoc> generatorExcel(List<String> classPaths,
                                        String excelPath, String user,
                                        String date, String version) {
        List<TestDoc> testDocs = new ArrayList<>();
        classPaths.forEach(classPath -> testDocs.addAll(readCode(classPath)));
        setCaseId(testDocs);
        testDocs.forEach(testDoc -> {
            testDoc.setTester(user);
            testDoc.setTestDate(date);
            testDoc.setVersion(version);
        });

        writeExcel(excelPath, testDocs, assertCounter.get());
        return testDocs;
    }

    private void setCaseId(List<TestDoc> testDocs) {
        Map<String, Integer> mainClassCounter = new HashMap<>(testDocs.size() * 2);
        testDocs.forEach(testDoc -> {
            mainClassCounter.merge(testDoc.getMainClassName(), 1, Integer::sum);
            testDoc.setCaseId(testDoc.getMainClassName() + "_" + mainClassCounter.get(testDoc.getMainClassName()));
        });
    }

    public void writeExcel(String fileName, List<TestDoc> testDocs, int assertCount) {
        try (ExcelWriter excelWriter = EasyExcel.write(fileName).build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("内存清算").head(TestDoc.class).build();
            List<TestData> testDataList = DocMapper.INSTANCE.toTestDataList(testDocs);
            testDocs.forEach(testDoc -> testDoc.setData(""));
            WriteSheet writeSheet2 = EasyExcel.writerSheet("@测试数据").head(TestData.class).build();
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            excelWriter.write(testDocs, writeSheet);
            excelWriter.write(testDataList, writeSheet2);
            WriteSheet writeSheet3 = EasyExcel.writerSheet("@统计").head(TestCount.class).build();
            TestCount testCount = new TestCount();
            testCount.setAssertCount(assertCount);
            excelWriter.write(List.of(testCount), writeSheet3);
        }

    }

    public void appendExcel(String fileName, List<TestDoc> testDocs) {
        String tmpFileName = fileName.replaceAll(".xlsx", "-tmp.xlsx");
        File file = new File(fileName);
        File tempFile = new File(tmpFileName);
        if (file.exists()) {
            // 第二次按照原有格式，不需要表头，追加写入
            EasyExcel.write(file, TestDoc.class).needHead(false).
                    withTemplate(file).file(tempFile).sheet().doWrite(testDocs);
        } else {
            // 第一次写入需要表头
            EasyExcel.write(file, TestDoc.class).sheet().doWrite(testDocs);
        }

        if (tempFile.exists()) {
            file.delete();
            tempFile.renameTo(file);
        }

    }

    public List<TestDoc> readCode(String classPath) {
        log.info("readCode of class,{}", classPath);
        File file = new File(classPath);
        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(file);
        } catch (FileNotFoundException e) {
            log.error("readCode err ,{}", classPath, e);
        }
        return parseTestMethods(cu);
    }

    /**
     * 测试用例： 有@InjectMocks注释的属性，从开始向下自增。done
     * 方法、函数： 取test***01()中的***首字母小写。 done
     * 测试目的描述： 测试目的 直接获取 done
     * 测试步骤概述： 固定为启动清算，检查测试状态 done
     * 测试数据：  从方法体中获取  从//data和//stub之间获取，为空则从setUp中获取。 todo
     * 期望结果： 期望结果 直接获取 done
     * 实际结果： 取 期望结果 的结果 done
     * 测试结果：都为pass
     * 测试人员：获取配置
     * 测试日期：获取配置
     * 测试版本：获取配置
     */
    private List<TestDoc> parseTestMethods(CompilationUnit cu) {
        List<TestDoc> testMethods = new ArrayList<>();
        // 类型声明
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
        for (TypeDeclaration<?> type : types) {
            // 成员,貌似getChildNodes方法也可行
            NodeList<BodyDeclaration<?>> members = type.getMembers();
            TestDoc setUp = null;
            for (BodyDeclaration<?> member : members) {
                TestDoc testDoc = parseTestMethodToDoc(member);
                if (null == testDoc || null == testDoc.getMethod()) {
                    continue;
                }
                if (!StringUtils.equals("initData", testDoc.getMethod())) {
                    testMethods.add(testDoc);
                } else {
                    setUp = testDoc;
                }
            }
            String mainClassName = getMainClassName(members);
            for (int i = 0; i < testMethods.size(); i++) {
                TestDoc testMethod = testMethods.get(i);
                testMethod.setMainClassName(mainClassName);
                testMethod.setCaseId(testMethod.getMainClassName() + "_" + (i + 1));
                if (StringUtils.isEmpty(testMethod.getData()) && setUp != null) {
                    testMethod.setData(setUp.getData());
                }
                assertCounter.getAndAdd(testMethod.getAssertCount());
            }
        }
        return testMethods;
    }


    private static String captureName(String name) {
        String ret = name.replaceAll("test", "")
                .replaceAll("\\d", "")
                .replaceAll("\\(", "").replaceAll("\\)", "");
        return ret.substring(0, 1).toLowerCase() + ret.substring(1);

    }

    /**
     * 获取被测试的类名
     *
     * @return 简单类名
     */
    private String getMainClassName(NodeList<BodyDeclaration<?>> members) {
        String name = "";
        for (Node node : members) {

            if (node instanceof FieldDeclaration) {
                // 成员变量声明
                // do something with this field declaration
                // 注释
                Comment comment = node.getComment().orElse(null);
                if (null != comment && comment.asString().contains("被测试类")) {
                    NodeList<VariableDeclarator> variables = ((FieldDeclaration) node).getVariables();
                    return variables.get(0).getTypeAsString();
                }
            }
        }
        return name;
    }

    /**
     * 处理类型,方法,成员
     *
     * @param node
     */
    private TestDoc parseTestMethodToDoc(Node node) {
        if (!(node instanceof MethodDeclaration)) {
            return null;
        }
        TestDoc testDoc = new TestDoc();
        // 方法声明
        // do something with this method declaration
        MethodDeclaration method = (MethodDeclaration) node;
        String methodName = captureName(method.getName().asString());
        if (method.getAnnotationByName("Test").isPresent()) {
            //测试方法则加入进来
            testDoc.setMethod(methodName);
            node.getComment().ifPresent(comment -> {
                Matcher matcher = pattern1.matcher(comment.getContent());
                matcher.results().forEach(m -> {
                            if (m.group(1).equals("目的")) {
                                testDoc.setDesc(m.group(2).replaceAll("\\*", ""));
                            }
                            if (m.group(1).equals("期望结果")) {
                                testDoc.setResult(m.group(2).replaceAll("\\*", ""));
                                testDoc.setExpect(m.group(2).replaceAll("\\*", ""));
                            }
                        }
                );
                testDoc.setStep("启动清算，检查测试状态");
                testDoc.setData(getDataBody(method));
                testDoc.setAssertCount(getAssertCount(method));
                testDoc.setTestResult("pass");
            });
        }

        if (StringUtils.equals("initData", methodName)) {
            testDoc.setData(method.getBody().get().getStatements().toString());
            testDoc.setMethod(methodName);
        }
        return testDoc;
    }

    private String getDataBody(MethodDeclaration method) {
        String data = "";
        if (method.getBody().isPresent()) {
            String body = method.getBody().get().getStatements().toString();
            Matcher matcher2 = pattern2.matcher(body);
            if (matcher2.find()) {
                String ret = matcher2.group(1).trim();
                data = StringUtils.isNotBlank(ret) ? ret : "";
            } else {
                log.warn("method {} has no data mock", method.getName());
            }
        }
        return data;
    }

    private int getAssertCount(MethodDeclaration method) {
        if (method.getBody().isEmpty()) {
            return 0;
        }
        List<String> asserts = new LinkedList<>();
        method.getBody().get().getStatements().forEach(statement -> {
            if (statement instanceof ExpressionStmt) {
                ExpressionStmt expressionStmt = (ExpressionStmt) statement;
                Expression expression = expressionStmt.getExpression();
                if (expression.isMethodCallExpr()) {
                    MethodCallExpr methodCallExpr = expression.asMethodCallExpr();
                    if (isAssert(methodCallExpr.getNameAsString())) {
                        asserts.add(expression.toString());
                    }
                }
            }
        });
        log.debug("assert expression:{}", asserts);
        return asserts.size();
    }

    private boolean isAssert(String st) {
        List<String> asserts = Arrays.asList("assert", "times(");
        for (String assertStr : asserts) {
            if (st.contains(assertStr)) {
                return true;
            }
        }
        return false;
    }
}
