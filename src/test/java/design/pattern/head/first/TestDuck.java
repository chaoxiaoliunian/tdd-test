package design.pattern.head.first;

import design.pattern.head.first.impl.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDuck {
    @Test
    public void testDuckChBehavior() {
        Duck duck = new ModelDuck();
        assertEquals("I can't fly!", duck.performFly());
        assertEquals("I'm quacking!", duck.performQuack());
        duck.setFlyBehavior(new FlyRocketPowered());
        assertEquals("I'm flying with a rocket!", duck.performFly());
    }

    @Test
    public void testDuck() {
        Duck mallard = new MallardDuck();
        assertEquals("I'm flying!", mallard.performFly());
        assertEquals("I'm quacking!", mallard.performQuack());
        assertEquals("All ducks float,even decoys!", mallard.swim());
    }

    @Test
    public void testModelDuck() {
        ModelDuck modelDuck = new ModelDuck();
        assertEquals("I can't fly!", modelDuck.performFly());
        assertEquals("I'm quacking!", modelDuck.performQuack());
    }

    @Test
    public void testFly() {
        assertEquals("I can't fly!", new FlyNoWay().fly());
        assertEquals("I'm flying with a rocket!", new FlyRocketPowered().fly());
    }

    @Test
    public void testQuack() {
        assertEquals("I'm quacking!", new Quack().quack());
        assertEquals("<<Silence>>", new MuteQuack().quack());
    }

    @Test
    public void testFlying() {
        FlyBehavior flyBehavior = new FlyWithWings();
        assertEquals("I'm flying!", flyBehavior.fly());
    }

    @Test
    public void testMallardDuck() {
        MallardDuck mallardDuck = new MallardDuck();
        assertEquals("绿头鸭", mallardDuck.display());
    }
}
