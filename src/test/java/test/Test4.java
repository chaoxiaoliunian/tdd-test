package test;

import org.junit.Test;
import tdd.test.test4.Dollar;

import static org.junit.Assert.*;
/**
 * 当瑞士法郎与美元的汇率为2：1,5美元+10瑞士法郎=10美元
 * 5美元*2=10美元
 * 将amount定义为私有 done
 * Dollar副作用——外部操作可以直接改变类的状态？ done
 * 钱必须整数？
 * 实现equals函数，这样就不需要访问amount了，就能解决私有问题。（值对象必须实现equals函数，且值对象不能有副作用，就像两张钱一样，不能改5美元为10美元） done
 * 考虑到hash表，实现equals函数还需要实现hashCode函数
 */
public class Test4 {
    /**
     *  5美元*2=10美元
     */
    @Test
    public void testMultiplication() {
        Dollar five = new Dollar(5);
        assertEquals(new Dollar(10), five.times(2));
        //测试是否有副作用即 5*2后计算5*3的结果是15，后一步不受之前的影响，即外部的操作不影响类的内部状态
        /**
         * 将一个设计缺陷转化为一个由此缺陷导致运行失败的测试程序
         * 采用存根实现代码快速编译通过（这里的存根不太理解，我理解的就是补充缺失的类）
         * 键入代码以使程序能尽快工作
         */
        assertEquals(new Dollar(15), five.times(3));
    }
    @Test
    public void testEquals() {
        Dollar five = new Dollar(5);
        assertTrue(five.equals(new Dollar(5)));
        //三角法，先用伪实现，当引入>=2个测试用例的时候才进行一般化
        /**
         * 注意到equals
         * 测试equals
         * 给简单的实现
         * 不急着重构，进一步测试他
         * 一般化（重构，解决测试的重复设计）
         */
        assertFalse(five.equals(new Dollar(6)));
    }
}
