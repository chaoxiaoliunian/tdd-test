package design.pattern.head.first.impl;

import design.pattern.head.first.FlyBehavior;

public class FlyWithWings implements FlyBehavior {
    @Override
    public String fly() {
        return "I'm flying!";
    }
}
