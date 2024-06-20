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
public class TestData {
    @ExcelProperty("测试用例ID")
    private String caseId;
    /**
     * 主类名称
     */
    @ExcelIgnore
    private String mainClassName;

    @ExcelProperty("方法/函数")
    private String method;

    @ExcelProperty("测试数据")
    private String data;
}
