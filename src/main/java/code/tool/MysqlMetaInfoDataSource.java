package code.tool;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qishaojun
 */
public class MysqlMetaInfoDataSource implements MetaInfoDataSource {
    static Connection con; // 声明Connection对象
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

    public static Connection getConnection() { // 建立返回值为Connection的方法
        try { // 加载数据库驱动类
            Class.forName(driver);
            System.out.println("数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try { // 通过访问数据库的URL获取数据库连接对象
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + schema + "?useUnicode=true&characterEncoding=gbk", user, password);
            System.out.println("数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con; // 按方法要求返回一个Connection对象
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
            throw new RuntimeException(e);
        } finally {
            try {
                assert rs != null;
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return fields;
    }

    private String getType(String dbType) {
        String type = typeMap.get(dbType);
        return Strings.isNullOrEmpty(type) ? dbType : type;
    }
}
