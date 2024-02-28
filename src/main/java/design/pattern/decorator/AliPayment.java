package design.pattern.decorator;

public class AliPayment implements Payment {
    @Override
    public void pay(int i) {
        money.add(i);
    }

    @Override
    public int query() {
        return money.stream().mapToInt(Integer::intValue).sum();
    }
}
