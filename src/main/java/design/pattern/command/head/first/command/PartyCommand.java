package design.pattern.command.head.first.command;

import design.pattern.command.head.first.command.Command;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PartyCommand implements Command {
    List<Command> commands;

    public PartyCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String execute() {
        StringBuilder sb = new StringBuilder();
        this.commands.forEach(command -> sb.append(command.execute()).append(","));
        return sb.isEmpty() ? "" : sb.deleteCharAt(sb.length() - 1).toString();
    }

    @Override
    public String undo() {
        StringBuilder sb = new StringBuilder();
        List<Command> list = new LinkedList<>(this.commands);
        Collections.reverse(list);
        list.forEach(command -> sb.append(command.undo()).append(","));
        return sb.isEmpty() ? "" : sb.deleteCharAt(sb.length() - 1).toString();
    }
}
