package design.pattern.head.first.impl;

import design.pattern.head.first.QuackBehavior;

public class Quack implements QuackBehavior {
    @Override
    public String quack() {
        return "I'm quacking!";
    }
}
