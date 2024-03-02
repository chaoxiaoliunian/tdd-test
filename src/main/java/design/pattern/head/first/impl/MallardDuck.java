package design.pattern.head.first.impl;

import design.pattern.head.first.Duck;

public class MallardDuck extends Duck {
    public MallardDuck() {
        super.flyBehavior = new FlyWithWings();
        super.quack = new Quack();
    }

    @Override
    public String display() {
        return "绿头鸭";
    }
}
