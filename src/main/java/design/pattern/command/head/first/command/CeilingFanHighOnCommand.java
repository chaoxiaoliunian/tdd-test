package design.pattern.command.head.first.command;

import design.pattern.command.head.first.CeilingFan;

public class CeilingFanHighOnCommand implements Command {
    private CeilingFan ceilingFan;

    public CeilingFanHighOnCommand(CeilingFan ceilingFan) {
        this.ceilingFan = ceilingFan;
        ceilingFan.high();
    }

    @Override
    public String execute() {
        return ceilingFan.on() + "," + ceilingFan.getSpeed();
    }


    @Override
    public String undo() {
        return ceilingFan.off();
    }
}
