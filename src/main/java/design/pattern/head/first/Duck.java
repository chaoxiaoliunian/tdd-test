package design.pattern.head.first;

public abstract class Duck {
    protected FlyBehavior flyBehavior;
    protected QuackBehavior quack;

    public String performQuack() {
        return quack.quack();
    }

    public String performFly() {
        return flyBehavior.fly();
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public void setQuack(QuackBehavior quack) {
        this.quack = quack;
    }

    public abstract String display();

    public String swim() {
        return "All ducks float,even decoys!";
    }
}
