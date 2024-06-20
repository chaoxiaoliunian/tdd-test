import org.example.PlyWoodMatch;
import org.junit.Test;
import org.mockito.InjectMocks;
import tool.model.TestDoc;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    String classPath;

    {
        classPath = this.getClass().getResource("/")
                .getPath().replaceAll("target/test-classes/", "src/test/java/TestDocTest.java");

    }

    /**
     * 测试excel生成
     * 目的: 测试excel正确性
     * 期望结果: excel能正常生成
     */
    @Test
    public void testMatch01() {
        //data
        String excelPath = "d:/test-doc.xlsx";
        //stub
        //List<TestDoc> list = new ExcelGenerator().generatorExcel(Collections.singletonList(classPath), excelPath, "", "", "");
        List<TestDoc> list = new ExcelGenerator().readCode(classPath);
        assertEquals(2, list.size());
        assertEquals("PlyWoodMatch_1", list.get(0).getCaseId());
        assertEquals("match", list.get(0).getMethod());
        assertEquals("测试excel正确性", list.get(0).getDesc());
        assertEquals("excel能正常生成", list.get(0).getResult());
        assertTrue(list.get(0).getData().contains("String excelPath = \"d:/test-doc.xlsx\";"));

    }

    /**
     * 测试中文冒号
     * 目的:测试中文冒号
     * 期望结果：中文冒号能被识别
     */
    @Test
    public void testNone() {
        //  data
        String excelPath = "d:/test-doc.xlsx";

        String excelPath2 = "d:/test-doc.xlsx";
        //  stub
        List<TestDoc> list = new ExcelGenerator().readCode(classPath);
        assertEquals(2, list.size());
        assertEquals("PlyWoodMatch_2", list.get(1).getCaseId());
        assertEquals("none", list.get(1).getMethod());
        assertEquals("测试中文冒号", list.get(1).getDesc());
        assertEquals("中文冒号能被识别", list.get(1).getResult());
    }
}
