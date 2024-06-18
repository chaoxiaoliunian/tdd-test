package code.tool;

import lombok.Getter;

import java.util.List;

/**
 * @author qishaojun
 */
@Getter
public class ClassMetaInfo {
    private final String name;
    private final List<FieldMetaInfo> fields;

    public ClassMetaInfo(String tableName,  List<FieldMetaInfo> columns) {
        this.name = tableName;
        this.fields = columns;
    }

}
