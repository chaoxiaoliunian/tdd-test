package design.pattern.decorator;

public class BillDecorator extends RefundDecorator {
    public BillDecorator(Payment aliPayment) {
        super(aliPayment);
    }

    public int total() {
        return super.query();
    }
}
