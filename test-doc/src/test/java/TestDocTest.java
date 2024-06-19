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
 *
 * @author qishaojun
 */
public class TestDocTest {
    /**
     * 被测试类
     */
    @InjectMocks
    PlyWoodMatch plyWoodMatch;


    /**
     * 测试excel生成
     * 目的: 测试excel正确性
     * 期望结果: excel能正常生成
     */
    @Test
    public void testMatch01() {
        String classPath = "E:\\workspace\\tdd-test\\test-doc\\src\\test\\java\\TestDocTest.java";
        String excelPath = "d:/test-doc" + System.currentTimeMillis() + ".xlsx";
        List<TestDoc> list = new ExcelGenerator().generatorExcel(classPath, excelPath,"","","");
        assertEquals(1, list.size());
        assertEquals("PlyWoodMatch_1", list.get(0).getId());
        assertEquals("match", list.get(0).getMethod());
        assertEquals("测试excel正确性", list.get(0).getDesc());
        assertEquals("excel能正常生成", list.get(0).getResult());

    }
}
