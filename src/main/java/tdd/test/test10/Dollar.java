package tdd.test.test10;

/**
 * 继承money类
 */
public class Dollar extends Money {

    public Dollar(int amount, String currency) {
        super(amount,currency);
    }

    public Money times(int multiplier) {
        return Money.dollar(amount * multiplier);
    }

}
