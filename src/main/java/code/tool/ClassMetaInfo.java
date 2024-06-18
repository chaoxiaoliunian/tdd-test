package code.tool;

import lombok.Getter;

import java.util.List;

/**
 * @author qishaojun
 */
@Getter
public class ClassMetaInfo {
    private final String name;
    private final TableType tableType;
    private final List<FieldMetaInfo> fields;

    public ClassMetaInfo(String tableName, TableType tableType, List<FieldMetaInfo> columns) {
        this.name = tableName;
        this.tableType = tableType;
        this.fields = columns;
    }

}
