package test;

import org.junit.Test;
import tdd.test.test2.Dollar;

import static org.junit.Assert.assertEquals;

/**
 * 当瑞士法郎与美元的汇率为2：1,5美元+10瑞士法郎=10美元
 * 5美元*2=10美元
 * 将amount定义为私有
 * Dollar副作用——外部操作可以直接改变类的状态？ done
 * 钱必须整数？
 */
public class Test2 {
    @Test
    public void testMultiplication() {
        Dollar five = new Dollar(5);
        Dollar product=five.times(2);
        assertEquals(10, product.amount);
        //测试是否有副作用即 5*2后计算5*3的结果是15，后一步不受之前的影响，即外部的操作不影响类的内部状态
        /**
         * 将一个设计缺陷转化为一个由此缺陷导致运行失败的测试程序
         * 采用存根实现代码快速编译通过（这里的存根不太理解，我理解的就是补充缺失的类）
         * 键入代码以使程序能尽快工作
         */
        product=five.times(3);
        assertEquals(15,product.amount);
    }

}
