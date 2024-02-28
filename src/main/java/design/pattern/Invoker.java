package design.pattern;

public class Invoker {
    private Command cmd;

    public Invoker(Command cmd) {
        this.cmd = cmd;
    }

    public void call() {
        cmd.execute();
    }
}
