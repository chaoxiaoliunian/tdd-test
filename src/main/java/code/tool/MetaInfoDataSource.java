package code.tool;

import java.util.List;

/**
 * @author qishaojun
 */
public interface MetaInfoDataSource {

    ClassMetaInfo getClassMetaInfo(String viewEvents);

    ClassMetaInfo getClassMetaInfoOfView(String viewName);
}
