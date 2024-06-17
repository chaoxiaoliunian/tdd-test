package code.tool.datasource;

public class CodeGeneratorTest {
    /**
     *todo:代码生成器完整功能
     * 核心业务功能：
     * 1.能够根据Oracle、Mysql视图和表，Excel文档生成Java类，且Java类的字段带有注释。
     * 2.能够生成加载方法。
     * 4.能够生成表与表的关联关系。
     * 设计：
     * 1.数据源和代码生成方法都是插件式的，copy到插件目录下就能自动运行。
     * 2.可打包成一个jar，java -jar 读取配置文件，生成代码。
     * 3.可启动一个web，引入后，访问网页使用功能。
     */
    /**
     *代码生成器简版功能
     * 核心业务功能：
     * 根据Mysql视图和表生成Java类，且Java类字段带有注释。
     * 设计：
     * 1.数据源和代码生成方法都是插件式的，copy到插件目录下就能自动运行。
     * 2.可通过main方法指定数据库地址、库，表运行。
     */
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=gbk";
        String user = "root";
        String password = "qishaojun1234";
        String driver = "com.mysql.cj.jdbc.Driver";
        String database = "test";
        String table = "test";
        String packageName = "code.tool.datasource";
        String className = "Test";
        String filePath = "D:\\test\\";
        String fileName = "Test.java";
        String comment= "测试";
    }
}
