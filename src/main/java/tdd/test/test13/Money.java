package tdd.test.test13;

public class Money implements Expression {

    /**
     * 从dollar中上移
     */
    protected int amount;
    protected String currency;

    public Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }


    public static Money dollar(int i) {
        return new Money(i, "USD");
    }

    public static Money franc(int i) {
        return new Money(i, "CHF");
    }

    public boolean equals(Object object) {
        //从dollar中上移，除了修改类型还要记得修改变量名
        Money money = (Money) object;
        /**
         * 希望引入货币——这一金融领域更有意义的设计而不是Java对象域的，不过这里目前是非必要的引入，暂时不做变更
         */
        return this.amount == money.amount && this.currency.equals(money.currency);
    }

    public String currency() {
        return currency;
    }

    public String toString() {
        return amount + " " + currency;
    }

    public Money times(int multiplier) {
        //解决的是有副作用的问题
        return new Money(amount * multiplier, currency);
    }

    public Expression plus(Money dollar) {
        return new Sum(this, dollar);
    }

    @Override
    public Money reduce(String to) {
        return this;
    }
}
