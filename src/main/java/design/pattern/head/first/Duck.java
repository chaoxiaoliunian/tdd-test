package design.pattern.head.first;

public abstract class Duck {
    protected FlyBehavior flyBehavior;
    protected QuackBehavior quack;

    public String performQuack() {
        //return "I'm quacking!";
        return quack.quack();
    }

    public String performFly() {
        return flyBehavior.fly();
    }

    public abstract String display();

    public String swim() {
        return "All ducks float,even decoys!";
    }
}
