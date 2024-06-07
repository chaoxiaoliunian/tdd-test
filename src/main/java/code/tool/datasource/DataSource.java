package code.tool.datasource;

import java.sql.Connection;

public interface DataSource {
    Connection getConnection();
}
