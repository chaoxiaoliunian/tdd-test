package code.tool.datasource;

import junit.framework.TestCase;

import java.util.Collection;

public class MetaInfoGetterTest extends TestCase {
    public void testGetTableMetaInfo() {
        MetaInfoGetter metaInfoGetter = new MetaInfoGetter();
        Collection<TableMetaInfo> databases = metaInfoGetter.getDatabases();
        assertEquals(10, databases.size());
    }

}