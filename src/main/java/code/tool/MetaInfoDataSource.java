package code.tool;

import java.util.List;

/**
 * @author qishaojun
 */
public interface MetaInfoDataSource {
    TableType getTableType(String table);

    List<FieldMetaInfo> getColumns(String table);
}
