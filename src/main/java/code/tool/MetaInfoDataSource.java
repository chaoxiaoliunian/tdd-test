package code.tool;

import java.util.List;

/**
 * @author qishaojun
 */
public interface MetaInfoDataSource {
    List<FieldMetaInfo> getColumns(String table);
}
