package tdd.test.test2;

public class Dollar {
    public int amount;

    public Dollar(int amount) {
        this.amount=amount;
    }

    public Dollar times(int multiplier) {
        //解决的是有副作用的问题
        return new Dollar(amount*multiplier);
    }
}
