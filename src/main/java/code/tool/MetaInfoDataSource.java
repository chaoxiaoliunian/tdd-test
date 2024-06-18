package code.tool;

import java.util.List;

/**
 * @author qishaojun
 */
public interface MetaInfoDataSource {
    /**
     * 获取表基础信息
     *
     * @param table 表名称
     * @return 表基础信息
     */
    List<FieldMetaInfo> getColumns(String table);
}
