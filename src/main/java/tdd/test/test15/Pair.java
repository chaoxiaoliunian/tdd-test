package tdd.test.test15;

public class Pair {
    private String from;
    private String to;

    public Pair(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public boolean equals(Object object) {
        Pair pair = (Pair) object;
        return this.from.equals(pair.from) && this.to.equals(pair.to);
    }

    public int hashCode() {
        return 0;
    }
}
