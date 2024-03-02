package design.pattern.head.first.impl;

import design.pattern.head.first.Duck;

public class ModelDuck extends Duck {
    public ModelDuck() {
        flyBehavior = new FlyNoWay();
        quack = new Quack();
    }

    @Override
    public String display() {
        return "模型鸭";
    }
}
