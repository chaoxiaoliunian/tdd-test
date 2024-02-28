package test1;

import org.junit.Test;
import tdd.test.test1.Dollar;

import static org.junit.Assert.assertEquals;

/**
 * 当瑞士法郎与美元的汇率为2：1,5美元+10瑞士法郎=10美元
 * 5美元*2=10美元
 * 将amount定义为私有
 * Dollar副作用？
 * 钱必须整数？
 */
public class Test1 {
    @Test
    public void testMultiplication() {
        Dollar five = new Dollar(5);
        five.times(2);
        assertEquals(10, five.amount);
    }

}
