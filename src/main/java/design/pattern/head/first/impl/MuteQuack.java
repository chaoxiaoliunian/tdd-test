package design.pattern.head.first.impl;

import design.pattern.head.first.QuackBehavior;

public class MuteQuack implements QuackBehavior {
    @Override
    public String quack() {
        return "<<Silence>>";
    }
}
