package test;

import org.junit.Test;
import tdd.test.test5.Dollar;
import tdd.test.test5.Franc;

import static org.junit.Assert.*;

/**
 * 当瑞士法郎与美元的汇率为2：1,5美元+10瑞士法郎=10美元
 * 5美元*2=10美元 done
 * 将amount定义为私有 done
 * Dollar副作用——外部操作可以直接改变类的状态？ done
 * 钱必须整数？
 * 实现equals函数，这样就不需要访问amount了，就能解决私有问题。（值对象必须实现equals函数，且值对象不能有副作用，就像两张钱一样，不能改5美元为10美元） done
 * 考虑到hash表，实现equals函数还需要实现hashCode函数
 * 为了解决第一个问题，得先有一个类似美元的瑞士法郎：5瑞士*2=10瑞士法郎 done
 * 消除瑞士法郎和美元的重复设计
 * 新增：瑞士法郎的普通判等（因为要统一法郎和美元） done
 * 瑞士法郎的普通相乘（因为要统一法郎和美元）
 * 新增：如何比较法郎和美元
 */
public class Test6 {
    /**
     *  5法郎*2=10法郎
     *  无法完成更大的测试，所以新增了一个小测试
     *  无所顾忌的复制粘贴测试
     *  无所顾忌的复制粘贴实现
     *  需要进一步的消除重复设计
     */
    @Test
    public void testFrancMultiplication() {
        Franc five = new Franc(5);
        assertEquals(new Franc(10), five.times(2));
        assertEquals(new Franc(15), five.times(3));
    }
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

    /**
     * 测试equals
     */
    @Test
    public void testEquals() {
        assertTrue(new Dollar(5).equals(new Dollar(5)));
        //三角法，先用伪实现，当引入>=2个测试用例的时候才进行一般化
        /**
         * 注意到equals
         * 测试equals
         * 给简单的实现
         * 不急着重构，进一步测试他
         * 一般化（重构，解决测试的重复设计）
         */
        assertFalse(new Dollar(5).equals(new Dollar(6)));
        /**
         * 为了将equals替换掉法郎，需要先建立测试，然后就可以让法郎继承money了
         *
         */
        assertTrue(new Franc(5).equals(new Franc(5)));
        assertFalse(new Franc(5).equals(new Franc(6)));
    }
}
