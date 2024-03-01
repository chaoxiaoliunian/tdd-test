package tdd.test.test15;

public interface Expression {
    Money reduce(Bank bank, String to);

    Expression plus(Expression tenFranc);
}
