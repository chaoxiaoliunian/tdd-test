package code.tool;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.protobuf.StringValue;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
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
    public static final String SPLITTER = ".";
    public static final String SPLITTER_REX = "\\.";

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
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + schema + "?useUnicode=true&characterEncoding=gbk&useInformationSchema=true", user, password);
            log.info("get conn success");
        } catch (SQLException e) {
            log.error("get conn fail", e);
        }
        return con;
    }

    private String getClassName(String table) {
        if (table.contains(SPLITTER)) {
            table = table.substring(table.lastIndexOf(SPLITTER) + 1);
        }
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table);
    }

    private String getType(String dbType) {
        String type = typeMap.get(dbType);
        return Strings.isNullOrEmpty(type) ? dbType : type;
    }

    @Override
    public ClassMetaInfo getClassMetaInfo(String tableName) {
        String tableSimpleName = tableName;
        String schemaName = schema;
        if (tableName.contains(SPLITTER)) {
            schemaName = tableName.split(SPLITTER_REX)[0];
            tableSimpleName = tableName.split(SPLITTER_REX)[1];
        }
        List<FieldMetaInfo> fields = Lists.newArrayList();
        String tableComment = "";
        ResultSet rs = null;
        try {
            rs = con.getMetaData().getTables(null, schemaName, tableSimpleName, new String[]{"TABLE", "VIEW"});
            if (rs.next()) {
                tableComment = rs.getString("REMARKS");
            }
            rs.close();
            rs = con.getMetaData().getColumns(null, schemaName, tableSimpleName, "%");
            while (rs.next()) {
                String name = rs.getString("COLUMN_NAME");
                String type = rs.getString("TYPE_NAME");
                String comment = rs.getString("REMARKS");
                fields.add(new FieldMetaInfo(name, comment, getType(type)));
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
        return new ClassMetaInfo(getClassName(tableName), tableComment, fields);
    }

    /**
     * 获取视图的基础表
     * @param viewName 视图名称
     * @return 基础表列表
     */
    public List<String> getBaseTables(String viewName) {
        List<String> tableNames = new ArrayList<>();
        String tableSimpleName = viewName;
        String schemaName = schema;
        if (viewName.contains(SPLITTER)) {
            schemaName = viewName.split(SPLITTER_REX)[0];
            tableSimpleName = viewName.split(SPLITTER_REX)[1];
        }
        ResultSet rs = null;
        try {
            String sql = "select * from INFORMATION_SCHEMA.VIEWS where TABLE_SCHEMA=? and TABLE_NAME=?;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, schemaName);
            preparedStatement.setString(2, tableSimpleName);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                tableNames = getAllTableNameBySQL(rs.getString("VIEW_DEFINITION"));
            }
        } catch (SQLException e) {
            log.error("get view table error", e);
        }
        //获取每个表的基础定义
        //根据视图中的字段，获取基础表的字段注释
        return tableNames;
    }

    private List<String> getAllTableNameBySQL(String sql) {
        SQLStatementParser parser = new MySqlStatementParser(sql);
        // 使用Parser解析生成AST，这里SQLStatement就是AST
        SQLStatement sqlStatement = parser.parseStatement();
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);
        Map<TableStat.Name, TableStat> tables = visitor.getTables();
        List<String> allTableName = new ArrayList<>();
        for (TableStat.Name t : tables.keySet()) {
            allTableName.add(t.getName());
        }
        return allTableName;
    }
}
