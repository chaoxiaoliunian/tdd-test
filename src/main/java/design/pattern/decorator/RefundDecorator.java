package design.pattern.decorator;

public class RefundDecorator implements Payment {
    Payment payment;

    public RefundDecorator(Payment payment) {
        this.payment = payment;
    }

    /**
     * 退款
     *
     * @param i
     */
    public void refund(int i) {
        payment.money.add(-1 * i);
    }


    @Override
    public void pay(int i) {
        payment.pay(i);
    }

    @Override
    public int query() {
        return payment.query();
    }
}
