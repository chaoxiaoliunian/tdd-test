package code.tool;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CodeGeneratorTest {
    MetaInfoDataSource dataSource=new MysqlMetaInfoDataSource();
    CodeGenerator codeGenerator=new CodeGenerator(dataSource);
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
     * 代码生成器简版功能
     * 核心业务功能：
     * 1.根据Mysql视图和表生成Java类，且Java类字段带有注释。
     * 根据表名称能够生成类
     * 根据字段能生成属性
     * 根据字段能生成get，set方法
     * 根据字段能生成注释
     * 能够生成继承BaseTable，COLUMNS属性，无参构造方法，序列化
     * 设计：
     * 1.数据源和代码生成方法都是插件式的，copy到插件目录下就能自动运行。
     * 2.可通过main方法指定数据库地址、库，表运行。
     */

    @Test
    public void testClassGenerator() {
        //能正常生成视图对应的类
        ClassMetaInfo classMetaInfo = codeGenerator.getClassMetaInfo("view_events");
        assertEquals("ViewEvents", classMetaInfo.getName());
        assertTableMetaInfo(classMetaInfo);
        //能正常生成表对应的类
        ClassMetaInfo classMetaInfo2 = codeGenerator.getClassMetaInfo("events");
        assertEquals("Events", classMetaInfo2.getName());
        assertTableMetaInfo(classMetaInfo2);
    }

    private static void assertTableMetaInfo(ClassMetaInfo classMetaInfo) {
        assertEquals("rev", classMetaInfo.getFields().get(0).name());
        assertEquals("String", classMetaInfo.getFields().get(0).type());
        assertEquals("partition", classMetaInfo.getFields().get(2).name());
        assertEquals("Integer", classMetaInfo.getFields().get(2).type());
        assertEquals("event", classMetaInfo.getFields().get(4).name());
        assertEquals("byte[]", classMetaInfo.getFields().get(4).type());
    }
}
