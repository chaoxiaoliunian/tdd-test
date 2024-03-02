package design.pattern.command.head.first;

public class CeilingFan {
    private int speed = 1;

    public String on() {
        return "电风扇开启";
    }

    public void medium() {
        speed = 2;
    }

    public void high() {
        speed = 3;
    }

    public void low() {
        speed = 1;
    }

    public String getSpeed() {
        return "风速:" + speed;
    }

    public String off() {
        return "电风扇关闭";
    }
}
