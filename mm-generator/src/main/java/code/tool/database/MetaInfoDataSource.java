package code.tool.database;

import code.tool.model.TableMetaInfo;

/**
 * @author qishaojun
 */
public interface MetaInfoDataSource {
    /**
     * 获取表的基本信息
     *
     * @param tableName 表名称
     * @return 表的基本信息
     */
    TableMetaInfo getTableMetaInfo(String tableName);

    /**
     * 获取视图的基本信息
     *
     * @param viewName 视图
     * @return 视图基本信息
     */
    TableMetaInfo getTableMetaInfoOfView(String viewName);
}
