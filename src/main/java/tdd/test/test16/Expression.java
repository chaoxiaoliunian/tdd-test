package tdd.test.test16;

public interface Expression {
    Money reduce(Bank bank, String to);

    Expression plus(Expression tenFranc);

    Expression times(int multiplier);
}
