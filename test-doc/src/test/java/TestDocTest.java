import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.example.PlyWoodMatch;
import org.junit.Test;
import org.mockito.InjectMocks;
import tool.model.TestDoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * TODO: 预计花费2小时
 * 1.支持任意的测试类生成
 * 2.打jar包，可指定任意的类。
 * 3.内网测试。
 * @author qishaojun
 */
public class TestDocTest {
    @InjectMocks
    PlyWoodMatch plyWoodMatch;
    static Pattern pattern1 = Pattern.compile("(目的|期望结果):\\s+(.*)");
    static Pattern pattern2 = Pattern.compile("([a-z,A-Z]+)\\d+\\(\\)$");

    static List<TestDoc> testMethods = new ArrayList<>();

    /**
     * 测试正则
     * 目的: 测试正则正确
     * 期望结果: 正则能够匹配成功
     */
    @Test
    public void test() {
        assertEquals("aA", captureName("testAA01()"));
        assertEquals("aA", captureName("aA()"));
    }

    public static String captureName(String name) {
        Matcher matcher = pattern2.matcher(name);
        if (matcher.find()) {
            name = matcher.group(1).replaceAll("test", "");
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        return name.replaceAll("\\(","").replaceAll("\\)","");

    }

    /**
     * 测试excel生成
     * 目的: 测试excel正确性
     * 期望结果: excel能正常生成
     */
    @Test
    public void test2() {
        File file = new File("C:\\Users\\qishaojun\\IdeaProjects\\tdd-test\\doc-generator\\src\\test\\java\\TestDocTest.java");
        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //获取所有的测试方法
        cu.accept(new MethodVisitor(), null);
        //设置测试id 被测试类+index
        setId(TestDocTest.class);
        String fileName = "d:/test-doc" + System.currentTimeMillis() + ".xlsx";

        try (ExcelWriter excelWriter = EasyExcel.write(fileName, TestDoc.class).build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            excelWriter.write(testMethods, writeSheet);
        }
    }

    private <T> void setId(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        String idPrefix = "";
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectMocks.class)) {
                idPrefix = field.getType().getSimpleName();
                break;
            }
        }
        for (int i = 0; i < testMethods.size(); i++) {
            testMethods.get(i).setId(idPrefix + "_" + i);
        }
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
            if (n.getAnnotationByName("Test").isPresent()) {
                //测试方法则加入进来
                TestDoc testDoc = new TestDoc();
                String methodName = captureName(n.getName().asString());
                testDoc.setMethod(methodName);


                n.getComment().ifPresent(comment -> {
                    Matcher matcher = pattern1.matcher(comment.getContent());
                    matcher.results().forEach(m -> {
                                if (m.group(1).equals("目的")) {
                                    testDoc.setDesc(m.group(2));
                                }
                                if (m.group(1).equals("期望结果")) {
                                    testDoc.setResult(m.group(2));
                                    testDoc.setExpect(m.group(2));
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
            super.visit(n, arg);
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
//            System.out.println("class:"+n.getName());
//            System.out.println("extends:"+n.getExtendedTypes());
//            System.out.println("implements:"+n.getImplementedTypes());

            super.visit(n, arg);
        }

        @Override
        public void visit(PackageDeclaration n, Void arg) {
            //System.out.println("package:"+n.getName());
            super.visit(n, arg);
        }
    }
}
