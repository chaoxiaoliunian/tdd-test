package design.pattern.command.head.first.command;

public class NoCommand implements Command {
    @Override
    public String execute() {
        return "";
    }

    @Override
    public String undo() {
        return "";
    }
}
