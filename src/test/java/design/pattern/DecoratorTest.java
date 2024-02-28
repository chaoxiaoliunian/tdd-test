package design.pattern;

import design.pattern.decorator.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DecoratorTest {
    Payment aliPayment = new AliPayment();
    Payment weChatPayment = new WeChatPayment();

    @Before
    public void setUp() {
        Payment.money.clear();
    }

    @Test
    public void testBill() {
        aliPayment.pay(100);
        weChatPayment.pay(10);
        assertEquals(110, aliPayment.query());
    }

    /**
     * 装饰器模式增加退款，这里用的是实现被扩展的类的接口的方式实现的装饰器
     */
    @Test
    public void testRefundDecorator() {
        RefundDecorator refundDecorator = new RefundDecorator(aliPayment);
        refundDecorator.pay(100);
        refundDecorator.refund(10);
        assertEquals(90, refundDecorator.query());
    }

    /**
     * 装饰器，扩展对账后的总数
     */
    @Test
    public void testBillDecorator() {
        BillDecorator billDecorator = new BillDecorator(aliPayment);
        billDecorator.pay(100);
        billDecorator.refund(10);
        assertEquals(90, billDecorator.total());
    }
}
