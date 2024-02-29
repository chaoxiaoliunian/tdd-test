package tdd.test.test7;

public class Franc extends Money {
    /**
     * amount是完全一致的直接上移就行
     */
   // private int amount;

    public Franc(int amount) {
        this.amount = amount;
    }

    public Franc times(int multiplier) {
        //解决的是有副作用的问题
        return new Franc(amount * multiplier);
    }

}
