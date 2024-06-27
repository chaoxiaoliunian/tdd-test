package tool.model;

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
    @ExcelProperty("断言次数")
    private Integer assertCount;
}
