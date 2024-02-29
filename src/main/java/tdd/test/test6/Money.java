package tdd.test.test6;

public class Money {
    /**
     * 从dollar中上移
     */
    protected int amount;
    public boolean equals(Object object) {
        //从dollar中上移，除了修改类型还要记得修改变量名
        Money money = (Money) object;
        return this.amount == money.amount;
    }

}
