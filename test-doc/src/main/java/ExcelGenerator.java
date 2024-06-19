import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import tool.model.TestDoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelGenerator {
    static Pattern pattern1 = Pattern.compile("(目的|期望结果):\\s+(.*)");
//    static Pattern pattern2 = Pattern.compile("([a-z,A-Z]+)\\d+\\(\\)$");

    static List<TestDoc> testMethods = new ArrayList<>();

    public List<TestDoc> generatorExcel(String classPath,
                                        String excelPath, String user,
                                        String date, String version) {
        List<TestDoc> testDocs = readCode(classPath);
        testDocs.forEach(testDoc -> {
            testDoc.setTester(user);
            testDoc.setTestDate(date);
            testDoc.setVersion(version);
        });
        writeExcel(excelPath, testDocs);
        return testDocs;
    }

    public void writeExcel(String fileName, List<TestDoc> testDocs) {
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, TestDoc.class).build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("内存清算").build();
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            excelWriter.write(testDocs, writeSheet);
        }

    }

    public List<TestDoc> readCode(String classPath) {
        File file = new File(classPath);
        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //获取所有的测试方法
        parseOneFile(cu);

        return testMethods;
    }

    /**
     * 解析单个Java文件
     *
     * @param cu 编译单元
     */
    private void parseOneFile(CompilationUnit cu) {
        // 类型声明
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
        for (TypeDeclaration<?> type : types) {
            // 成员,貌似getChildNodes方法也可行
            NodeList<BodyDeclaration<?>> members = type.getMembers();
            members.forEach(this::processNode);
            String mainClassName = getMainClassName(members);
            for (int i = 0; i < testMethods.size(); i++) {
                testMethods.get(i).setId(mainClassName + "_" + (i + 1));
            }
        }

    }

    private static void generateTestDocModel(MethodDeclaration n) {
        if (n.getAnnotationByName("Test").isPresent()) {
            //测试方法则加入进来
            TestDoc testDoc = new TestDoc();
            String methodName = captureName(n.getName().asString());
            testDoc.setMethod(methodName);
            n.getComment().ifPresent(comment -> {
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
                testDoc.setData("");
                testDoc.setTestResult("pass");
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
            });
            testMethods.add(testDoc);
        }
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
    private void processNode(Node node) {
        if (node instanceof TypeDeclaration) {
            // 类型声明
            // do something with this type declaration

        } else if (node instanceof MethodDeclaration) {
            // 方法声明
            // do something with this method declaration
            generateTestDocModel((MethodDeclaration) node);

        }
    }


}
