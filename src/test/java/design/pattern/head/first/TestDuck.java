package design.pattern.head.first;

import design.pattern.head.first.impl.FlyWithWings;
import design.pattern.head.first.impl.MallardDuck;
import design.pattern.head.first.impl.Quack;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDuck {
    @Test
    public void testDuck() {
        Duck mallard = new MallardDuck();
        assertEquals("I'm flying!", mallard.performFly());
        assertEquals("I'm quacking!", mallard.performQuack());
        assertEquals("All ducks float,even decoys!", mallard.swim());
    }
    @Test
    public void testQuack(){
        QuackBehavior quackBehavior=new Quack();
        assertEquals("I'm quacking!",quackBehavior.quack());
    }
    @Test
    public void testFlying() {
        FlyBehavior flyBehavior=new FlyWithWings();
        assertEquals("I'm flying!",flyBehavior.fly());
    }

    @Test
    public void testMallardDuck() {
        MallardDuck mallardDuck = new MallardDuck();
        assertEquals("绿头鸭", mallardDuck.display());
    }
}
