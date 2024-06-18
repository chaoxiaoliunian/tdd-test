package code.tool;

import com.google.common.base.CaseFormat;

/**
 * @author qishaojun
 */
public class CodeGenerator {
    private final MetaInfoDataSource dataSource;

    public CodeGenerator(MetaInfoDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ClassMetaInfo getClassMetaInfo(String table) {
        String tableName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table);
        return new ClassMetaInfo(tableName, dataSource.getColumns(table));
    }
}
