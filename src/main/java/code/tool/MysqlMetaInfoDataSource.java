package code.tool;

import java.util.List;

/**
 * @author qishaojun
 */
public class MysqlMetaInfoDataSource implements MetaInfoDataSource {
    @Override
    public TableType getTableType(String table) {
        return null;
    }

    @Override
    public List<FieldMetaInfo> getColumns(String table) {
        return null;
    }
}
