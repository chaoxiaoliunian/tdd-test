package tdd.test.test8;

public abstract class Money {

    /**
     * 从dollar中上移
     */
    protected int amount;

    public static Money dollar(int i) {
        return new Dollar(i);
    }
    public abstract Money times(int multiplier);
    public boolean equals(Object object) {
        //从dollar中上移，除了修改类型还要记得修改变量名
        Money money = (Money) object;
        /**
         * 希望引入货币——这一金融领域更有意义的设计而不是Java对象域的，不过这里目前是非必要的引入，暂时不做变更
         */
        return this.amount == money.amount && this.getClass().equals(object.getClass());
    }

}
