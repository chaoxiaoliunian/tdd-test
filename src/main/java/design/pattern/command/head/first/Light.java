package design.pattern.command.head.first;

public class Light {
    private String location;

    public Light(String location) {
        this.location = location;
    }

    public String on() {
        return location + "灯开启";
    }

    public String off() {
        return location + "灯关闭";
    }
}
