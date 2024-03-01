package test;

import org.junit.Test;
import tdd.test.test16.Bank;
import tdd.test.test16.Expression;
import tdd.test.test16.Money;
import tdd.test.test16.Sum;

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
 * 消除瑞士法郎和美元的重复设计 done
 * 瑞士法郎的普通判等（因为要统一法郎和美元） done
 * 瑞士法郎的普通相乘（因为要统一法郎和美元） done
 * 比较法郎对象和美元对象 done
 * 货币？ done
 * 删除testFrancMultiplication done
 */

/**
 * 新的清单
 * 当瑞士法郎与美元的汇率为2：1,5美元+10瑞士法郎=10美元 done
 * 5美元+5美元=10美元 done
 * 从5美元+5美元返回一个Money对象
 * Bank.reduce(Money) done
 * 带换算的Reduce Money done
 * Reduce(Bank,String) done
 * Sum.plus
 * Expression.times()
 */
public class Test16 {
    @Test
    public void testSumTimes(){
        Expression fiveBucks=Money.dollar(5);
        Expression tenFranc=Money.franc(10);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Expression sum=new Sum(fiveBucks,tenFranc).times(2);
        Money result=bank.reduce(sum,"USD");
        assertEquals(Money.dollar(20),result);
    }
    @Test
    public void testSumPlusMoney(){
        Expression fiveBucks=Money.dollar(5);
        Expression tenFranc=Money.franc(10);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Expression sum=new Sum(fiveBucks,tenFranc).plus(fiveBucks);
        Money result=bank.reduce(sum,"USD");
        assertEquals(Money.dollar(15),result);
    }
    @Test
    public void testMixedAddition() {
        Expression fiveBucks=Money.dollar(5);
        Expression tenFranc=Money.franc(10);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result = bank.reduce(fiveBucks.plus(tenFranc), "USD");
        assertEquals(Money.dollar(10), result);
    }

    @Test
    public void testIdentityRate() {
        assertEquals(1, new Bank().rate("USD", "USD"));
    }

    /**
     * bank支持不同的币种之间的转换
     */
    @Test
    public void testReduceMoneyDifferentCurrency() {
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result = bank.reduce(Money.franc(2), "USD");
        assertEquals(Money.dollar(1), result);

        bank.addRate("RMB", "USD", 6);
        result = bank.reduce(new Money(6, "RMB"), "USD");
        assertEquals(Money.dollar(1), result);
    }

    /**
     * 让bank除了表达式外 还可以支持money
     */
    @Test
    public void testReduceMoney() {
        Bank bank = new Bank();
        Money result = bank.reduce(Money.dollar(1), "USD");
        assertEquals(Money.dollar(1), result);
    }

    /**
     * 当都是美元相加时，bank+sum可以正确计算
     */
    @Test
    public void testReduceSum() {
        Sum sum = new Sum(Money.dollar(4), Money.dollar(3));
        Bank bank = new Bank();
        Money result = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(7), result);
    }

    /**
     * sum用来记录表达式的
     */
    @Test
    public void testPlusReturnSum() {
        Money five = Money.dollar(5);
        Expression expression = five.plus(five);
        Sum sum = (Sum) expression;
        assertEquals(five, sum.augend);
        assertEquals(five, sum.addend);
    }

    /**
     * 5美元+5美元=10美元
     * bank.reduce需要执行表达式
     */
    @Test
    public void testSimpleAddition() {
//        Money sum=Money.dollar(5).plus(Money.dollar(5));
//        assertEquals(Money.dollar(10),sum);

        Money five = Money.dollar(5);
        Expression sum = five.plus(five);
        Bank bank = new Bank();
        Money reduced = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(10), reduced);

    }

    /**
     * 通过引入货币，可以消除重复的子类
     */
    @Test
    public void testCurrency() {
        assertEquals("USD", Money.dollar(1).currency());
        assertEquals("CHF", Money.franc(1).currency());

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
        //删除dollar后，也删除多余的测试
        assertTrue(Money.dollar(5).equals(Money.dollar(5)));
        assertFalse(Money.dollar(5).equals(Money.dollar(6)));
        assertFalse(Money.franc(5).equals(Money.dollar(5)));

    }
}
