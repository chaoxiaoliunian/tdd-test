package design.pattern.head.first.impl;

import design.pattern.head.first.FlyBehavior;

public class FlyNoWay implements FlyBehavior {
    @Override
    public String fly() {
        return "I can't fly!";
    }
}
