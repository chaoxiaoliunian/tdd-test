package tdd.test.test6;

public class Franc {
    /**
     * 因为equals的引入，解决了amount的私有性问题
     */
    private int amount;

    public Franc(int amount) {
        this.amount = amount;
    }

    public Franc times(int multiplier) {
        //解决的是有副作用的问题
        return new Franc(amount * multiplier);
    }

    /**
     * 补充equals复合值对象的规范
     */
    public boolean equals(Object object) {
        //解决了equals相等的问题
        Franc dollar = (Franc) object;
        return this.amount == dollar.amount;
    }
}
