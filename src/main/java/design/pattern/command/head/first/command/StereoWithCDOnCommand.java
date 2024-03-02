package design.pattern.command.head.first.command;

import design.pattern.command.head.first.StereoWithCD;

public class StereoWithCDOnCommand implements Command {
    private StereoWithCD stereoWithCD;

    public StereoWithCDOnCommand(StereoWithCD stereoWithCD, int volume) {
        this.stereoWithCD = stereoWithCD;
        this.stereoWithCD.setVolume(volume);
    }

    @Override
    public String execute() {
        return this.stereoWithCD.on();
    }

    @Override
    public String undo() {
        return this.stereoWithCD.off();
    }
}
