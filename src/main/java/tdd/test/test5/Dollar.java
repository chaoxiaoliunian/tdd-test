package tdd.test.test5;

public class Dollar {
    /**
     * 因为equals的引入，解决了amount的私有性问题
     */
    private int amount;

    public Dollar(int amount) {
        this.amount = amount;
    }

    public Dollar times(int multiplier) {
        //解决的是有副作用的问题
        return new Dollar(amount * multiplier);
    }

    /**
     * 补充equals复合值对象的规范
     */
    public boolean equals(Object object) {
        //解决了equals相等的问题
        Dollar dollar = (Dollar) object;
        return this.amount == dollar.amount;
    }
}
