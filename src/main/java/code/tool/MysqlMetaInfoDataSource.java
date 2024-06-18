package code.tool;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qishaojun
 */
@Slf4j
public class MysqlMetaInfoDataSource implements MetaInfoDataSource {
    static Connection con;
    public static String user = "root";
    public static String password = "qishaojun1234";
    public static String schema = "mysql";
    public static String driver = "com.mysql.cj.jdbc.Driver";
    static Map<String, String> typeMap = new HashMap<>();

    static {
        con = getConnection();
        typeMap.put("VARCHAR", "String");
        typeMap.put("INT", "Integer");
        typeMap.put("MEDIUMBLOB", "byte[]");
    }

    public static Connection getConnection() {
        try {
            Class.forName(driver);
            log.info("driver load success");
        } catch (ClassNotFoundException e) {
            log.error("driver load fail", e);
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + schema + "?useUnicode=true&characterEncoding=gbk", user, password);
            log.info("get conn success");
        } catch (SQLException e) {
            log.error("get conn fail", e);
        }
        return con;
    }

    @Override
    public List<FieldMetaInfo> getColumns(String table) {
        List<FieldMetaInfo> fields = Lists.newArrayList();
        ResultSet rs = null;
        try {
            DatabaseMetaData databaseMetaData = con.getMetaData();
            rs = databaseMetaData.getColumns(null, schema, table, "%");
            while (rs.next()) {
                String name = rs.getString("COLUMN_NAME");
                String type = rs.getString("TYPE_NAME");
                fields.add(new FieldMetaInfo(name, getType(type)));
            }
        } catch (SQLException e) {
            log.error("get columns fail", e);
        } finally {
            try {
                assert rs != null;
                rs.close();
            } catch (SQLException e) {
                log.error("rs close fail", e);
            }
        }
        return fields;
    }

    private String getType(String dbType) {
        String type = typeMap.get(dbType);
        return Strings.isNullOrEmpty(type) ? dbType : type;
    }
}
