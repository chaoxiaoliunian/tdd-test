package design.pattern.command.head.first;

import design.pattern.command.head.first.command.Command;
import design.pattern.command.head.first.command.NoCommand;

public class Invoker {
    Command command = new NoCommand();

    public void setCommand(Command command) {
        this.command = command;
    }

    public String invoke() {
        return command.execute();
    }

    public String undo() {
        return command.undo();
    }
}
