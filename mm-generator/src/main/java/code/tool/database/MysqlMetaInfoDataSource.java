package code.tool.database;

import code.tool.model.FieldMetaInfo;
import code.tool.model.TableMetaInfo;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qishaojun
 */
@Slf4j
public class MysqlMetaInfoDataSource implements MetaInfoDataSource {
    static Connection con;
    public static String user = "root";
    //public static String password = "qishaojun1234";
    public static String password = "root";

    public static String schema = "mysql";
    public static String driver = "com.mysql.cj.jdbc.Driver";
    public static final String SPLITTER = ".";
    public static final String SPLITTER_REX = "\\.";

    static Map<String, String> typeMap = new HashMap<>();

    static {
        con = getConnection();
        //TODO:扩容类型
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
            //TODO：支持oracle
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
    public TableMetaInfo getTableMetaInfo(String tableName) {
        String tableSimpleName = tableName;
        String schemaName = schema;
        //如包含.则拆分出数据库和表名称
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
        return new TableMetaInfo(getClassName(tableName), tableComment, fields);
    }

    @Override
    public TableMetaInfo getTableMetaInfoOfView(String viewName) {
        //获取到所有的基础表
        TableMetaInfo tableMetaInfo = getTableMetaInfo(viewName);
        List<String> baseTables = new ArrayList<>();
        //遍历基础表，获取到基础表的信息
        getBaseTables(viewName, baseTables);
        //遍历所有基础表，拼装视图的注释
        //遍历所有基础表，补充所有字段的注释
        StringBuilder viewComment = new StringBuilder();
        Map<String, FieldMetaInfo> viewFieldMap = tableMetaInfo.getFieldMap();
        List<FieldMetaInfo> fieldMetaInfoList = new ArrayList<>(tableMetaInfo.getFields().size());
        List<FieldMetaInfo> totalFieldMetaInfoList = new ArrayList<>();
        baseTables.forEach(tableName -> {
            TableMetaInfo subTableMetaInfo = getTableMetaInfo(tableName);
            //拼装基础表的注释信息作为视图的注释信息
            viewComment.append(tableName).append(":").append(subTableMetaInfo.getComment()).append(",");
            //如基础表字段存在于视图字段中，则获取到指定字段的注释
            totalFieldMetaInfoList.addAll(subTableMetaInfo.getFields());

        });
        if (viewComment.length() > 0) {
            viewComment.deleteCharAt(viewComment.length() - 1);
        }
        Map<String, FieldMetaInfo> totalFieldMetaInfoMap = totalFieldMetaInfoList.stream().collect(Collectors.toMap(FieldMetaInfo::getName, e -> e, (v1, v2) -> v1));
        viewFieldMap.forEach((k, v) -> fieldMetaInfoList.add(totalFieldMetaInfoMap.get(k)));
        return new TableMetaInfo(tableMetaInfo.getName(), viewComment.toString(), fieldMetaInfoList);
    }

    /**
     * 获取视图的基础表
     *
     * @param viewName 视图名称
     */
    private void getBaseTables(String viewName, List<String> baseTables) {
        List<String> tableNames = new ArrayList<>();
        String tableSimpleName = viewName;
        String schemaName = schema;
        if (viewName.contains(SPLITTER)) {
            schemaName = viewName.split(SPLITTER_REX)[0];
            tableSimpleName = viewName.split(SPLITTER_REX)[1];
        }
        ResultSet rs = null;
        try {
            //TODO 支持oracle查询基础表结构
            //select * from INFORMATION_SCHEMA.VIEWS where  TABLE_NAME='view_rental_customer_payment';
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
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("rs close fail", e);
            }
        }
        tableNames.forEach(tableName -> {
            if (isView(tableName)) {
                getBaseTables(tableName, baseTables);
            } else {
                baseTables.add(tableName);
            }
        });
    }

    private boolean isView(String viewName) {
        String tableSimpleName = viewName;
        String schemaName = schema;
        if (viewName.contains(SPLITTER)) {
            schemaName = viewName.split(SPLITTER_REX)[0];
            tableSimpleName = viewName.split(SPLITTER_REX)[1];
        }
        ResultSet rs = null;
        String tableType = "";
        try {
            rs = con.getMetaData().getTables(null, schemaName, tableSimpleName, new String[]{"TABLE", "VIEW"});
            if (rs.next()) {
                tableType = rs.getString("TABLE_TYPE");
            }
        } catch (SQLException e) {
            log.error("get table type fail", e);
        }
        return "VIEW".equals(tableType);
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
            allTableName.add(t.getName().replaceAll("`", ""));
        }
        return allTableName;
    }
}
