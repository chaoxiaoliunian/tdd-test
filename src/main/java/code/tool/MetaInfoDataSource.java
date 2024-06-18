package code.tool;

/**
 * @author qishaojun
 */
public interface MetaInfoDataSource {

    TableMetaInfo getTableMetaInfo(String viewEvents);

    TableMetaInfo getClassMetaInfoOfView(String viewName);
}
