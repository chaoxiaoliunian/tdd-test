package code.tool.database;

import code.tool.model.TableMetaInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MysqlMetaInfoDataSourceTest {
    MetaInfoDataSource dataSource = new MysqlMetaInfoDataSource();
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
//        TableMetaInfo tableMetaInfo = dataSource.getTableMetaInfo("view_events");
//        assertEquals("ViewEvents", tableMetaInfo.getName());
//        assertTableMetaInfo(tableMetaInfo);
//        //能正常生成表对应的类
//        TableMetaInfo tableMetaInfo2 = dataSource.getTableMetaInfo("events");
//        assertEquals("Events", tableMetaInfo2.getName());
//        assertTableMetaInfo(tableMetaInfo2);

        TableMetaInfo tableMetaInfo3 = dataSource.getTableMetaInfo("sakila.customer");
        assertEquals("Customer", tableMetaInfo3.getName());
        assertEquals(9, tableMetaInfo3.getFields().size());
    }

    /**
     * 判断表的元信息正确
     *
     * @param tableMetaInfo 表的元信息
     */
    private static void assertTableMetaInfo(TableMetaInfo tableMetaInfo) {
        assertEquals("rev", tableMetaInfo.getFields().get(0).getName());
        assertEquals("String", tableMetaInfo.getFields().get(0).getType());
        assertEquals("partition", tableMetaInfo.getFields().get(2).getName());
        assertEquals("Integer", tableMetaInfo.getFields().get(2).getType());
        assertEquals("event", tableMetaInfo.getFields().get(4).getName());
        assertEquals("byte[]", tableMetaInfo.getFields().get(4).getType());
    }

    /**
     * 测试获取表和视图的注释
     * alter table customer comment '顾客';
     * alter table payment comment '支付';
     * alter table payment  modify column `payment_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT comment '支付id';
     * <p>
     * create view view_customer_payment as
     * select a.*,b.`payment_id`,b.`staff_id` from customer a,payment b where a.`customer_id`=b.`customer_id`;
     */
    @Test
    public void testComment() {

        TableMetaInfo tableMetaInfo = dataSource.getTableMetaInfo("sakila.customer");
        assertEquals("顾客", tableMetaInfo.getComment());
        TableMetaInfo tableMetaInfo2 = dataSource.getTableMetaInfo("sakila.payment");
        assertEquals("支付id", tableMetaInfo2.getFields().get(0).getRemarks());
    }

    @Test
    public void testViewComment() {
        //SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEW_TABLE_USAGE WHERE VIEW_NAME = 'view_customer_payment';
        //create view view_rental_customer_payment as select a.rental_id,a.rental_date,a.inventory_id,b.* from rental a,view_customer_payment b where a.`customer_id`=b.`customer_id`;
        TableMetaInfo tableMetaInfo2 = dataSource.getTableMetaInfoOfView("sakila.view_rental_customer_payment");
        assertEquals("payment_id", tableMetaInfo2.getFields().get(12).getName());
        //视图的备注集成了各个基础表的备注
        assertEquals("sakila.rental:,sakila.customer:顾客,sakila.payment:支付", tableMetaInfo2.getComment());
        //视图的字段备注是各个基础表的备注
        assertEquals("支付id", tableMetaInfo2.getFields().get(12).getRemarks());

    }


}
