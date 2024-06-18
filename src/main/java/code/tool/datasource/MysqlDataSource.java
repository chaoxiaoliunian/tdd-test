package code.tool.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlDataSource { // 创建类Conn
    Connection con; // 声明Connection对象
    public static String user = "root";
    public static String password = "qishaojun1234";
    public static String driver = "com.mysql.cj.jdbc.Driver";

    public Connection getConnection() { // 建立返回值为Connection的方法
        try { // 加载数据库驱动类
            Class.forName(driver);
            System.out.println("数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try { // 通过访问数据库的URL获取数据库连接对象
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=gbk", user, password);
            System.out.println("数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con; // 按方法要求返回一个Connection对象
    }

    public static void main(String[] args) { // 主方法，测试连接
        MysqlDataSource c = new MysqlDataSource(); // 创建本类对象
        c.getConnection(); // 调用连接数据库的方法
    }
}


