package design.pattern.command.head.first;

public class StereoWithCD {
    private int volume;

    public String on() {
        return "音响开启";
    }

    public String setCD() {
        return "放入CD";
    }

    public String setVolume(int volume) {
        this.volume = volume;
        return "调节音量为" + volume;
    }

    public String off() {
        return "音响关闭";
    }
}
