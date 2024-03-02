package design.pattern.head.first.impl;

import design.pattern.head.first.FlyBehavior;

public class FlyRocketPowered implements FlyBehavior {
    @Override
    public String fly() {
        return "I'm flying with a rocket!";
    }
}
