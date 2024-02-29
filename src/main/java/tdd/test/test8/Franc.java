package tdd.test.test8;

public class Franc extends Money {
    /**
     * amount是完全一致的直接上移就行
     */
   // private int amount;

    public Franc(int amount) {
        this.amount = amount;
    }

    public Money times(int multiplier) {
        //解决的是有副作用的问题
        return new Franc(amount * multiplier);
    }

}
