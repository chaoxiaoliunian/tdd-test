package design.pattern.command.head.first.command;

import design.pattern.command.head.first.Light;

public class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light=light;
    }

    @Override
    public String execute() {
        return light.off();
    }

    @Override
    public String undo() {
        return light.on();
    }
}
