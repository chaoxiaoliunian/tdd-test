package tdd.test.test16;

import java.util.HashMap;

public class Bank {
    private HashMap<Pair, Integer> rates = new HashMap<>();

    public Money reduce(Expression source, String to) {
        return source.reduce(this, to);
    }

    public void addRate(String from, String to, int rate) {
        rates.put(new Pair(from, to), rate);
    }

    public int rate(String currency, String to) {
        if (currency.equals(to)) {
            return 1;
        }
        return this.rates.get(new Pair(currency, to));
    }
}
