package tdd.test.test15;

public class Sum implements Expression {
    public Expression augend;
    public Expression addend;

    public Sum(Expression augend, Expression addend) {
        this.augend = augend;
        this.addend = addend;
    }

    @Override
    public Money reduce(Bank bank, String to) {
        return new Money(augend.reduce(bank, to).amount + addend.reduce(bank, to).amount, to);
    }

    @Override
    public Expression plus(Expression tenFranc) {
        //这种没有逻辑的实现就叫stub
        return null;
    }
}
