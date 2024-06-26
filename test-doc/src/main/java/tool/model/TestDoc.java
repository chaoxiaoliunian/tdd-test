package tool.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author qishaojun
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TestDoc {
    /**
     * 一个方法内 assert或者times的次数
     */
    @ExcelIgnore
    private int assertCount;

    @ExcelProperty("测试用例ID")
    private String caseId;
    /**
     * 主类名称
     */
    @ExcelIgnore
    private String mainClassName;

    @ExcelProperty("方法/函数")
    private String method;

    @ExcelProperty("正常系/异常系")
    private String path;

    @ExcelProperty("前提条件")
    private String pre;

    @ExcelProperty("测试目的描述")
    private String desc;

    @ExcelProperty("测试步骤描述")
    private String step;

    @ExcelProperty("测试数据")
    private String data;

    @ExcelProperty("期望结果")
    private String expect;
    @ExcelProperty("实际结果")
    private String result;

    @ExcelProperty("测试结果")
    private String testResult;

    @ExcelProperty("测试人员")
    private String tester;

    @ExcelProperty("测试日期")
    private String testDate;

    @ExcelProperty("测试版本")
    private String version;

    @ExcelProperty("备注")
    private String remarks;

}
