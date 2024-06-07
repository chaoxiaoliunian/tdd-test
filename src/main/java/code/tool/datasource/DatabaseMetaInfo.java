package code.tool.datasource;

import java.util.Collection;

/**
 * @author qishaojun
 */
public record DatabaseMetaInfo(String databaseName, Collection<TableMetaInfo> tables) {
}
