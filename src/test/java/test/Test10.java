package test;

import org.junit.Test;
import tdd.test.test9.Money;

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
 * 瑞士法郎的普通判等（因为要统一法郎和美元） done
 * 瑞士法郎的普通相乘（因为要统一法郎和美元）
 * 比较法郎对象和美元对象 done
 * 货币？
 * 删除testFrancMultiplication
 */
public class Test10 {
    /**
     * 通过引入货币，可以消除重复的子类
     */
    @Test
    public void testCurrency(){
        assertEquals("USD",Money.dollar(1).currency());
        assertEquals("CHF",Money.franc(1).currency());

    }
    /**
     * 5法郎*2=10法郎
     * 无法完成更大的测试，所以新增了一个小测试
     * 无所顾忌的复制粘贴测试
     * 无所顾忌的复制粘贴实现
     * 需要进一步的消除重复设计
     */
    @Test
    public void testFrancMultiplication() {
        Money five = Money.franc(5);
        assertEquals(Money.franc(10), five.times(2));
        assertEquals(Money.franc(15), five.times(3));
    }

    /**
     * 5美元*2=10美元
     * 突出一个任务分解和小步子原理：
     * 如何消除times呢？ 将方法的签名统一，利用工厂消除了dollar的外部引用。至少将方法的签名移动到了父类中
     */
    @Test
    public void testMultiplication() {
        Money five = Money.dollar(5);
        assertEquals(Money.dollar(10), five.times(2));
        assertEquals(Money.dollar(15), five.times(3));
    }

    /**
     * 测试equals
     */
    @Test
    public void testEquals() {
        assertTrue(Money.dollar(5).equals(Money.dollar(5)));
        //三角法，先用伪实现，当引入>=2个测试用例的时候才进行一般化
        /**
         * 注意到equals
         * 测试equals
         * 给简单的实现
         * 不急着重构，进一步测试他
         * 一般化（重构，解决测试的重复设计）
         */
        assertFalse(Money.dollar(5).equals(Money.dollar(6)));
        /**
         * 为了将equals替换掉法郎，需要先建立测试，然后就可以让法郎继承money了
         *
         */
        assertTrue(Money.franc(5).equals(Money.franc(5)));
        assertFalse(Money.franc(5).equals(Money.franc(6)));
        /**
         *这里先用getClass解决两种货币的比较，除非有更好的动机否则不要引入更多的设计
         */
        assertFalse(Money.franc(5).equals(Money.dollar(5)));

    }
}
