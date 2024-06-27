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
public class TestCount {

    @ExcelProperty("测试用例ID")
    private String caseId;
    /**
     * 主类名称
     */
    @ExcelIgnore
    private String mainClassName;

    @ExcelProperty("方法/函数")
    private String method;

    /**
     * 一个方法内 assert或者times的次数
     */
    @ExcelProperty("断言次数")
    private Integer assertCount;

}
