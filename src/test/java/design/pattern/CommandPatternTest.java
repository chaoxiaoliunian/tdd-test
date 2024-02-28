package design.pattern;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CommandPatternTest {
    Receiver receiver = mock(Receiver.class);

    @Test
    public void testCommandPattern() {
        //测试命令执行者被执行了
        Command cmd = new ConcreteCommand(receiver);
        Invoker invoker = new Invoker(cmd);
        invoker.call();
        verify(receiver).action();
    }

    /**
     * 测试命令都实现了execute接口
     */
    @Test
    public void testCommandExecute() {
        Command cmd = mock(ConcreteCommand.class);
        cmd.execute();
        verify(cmd).execute();
    }

    @Test
    public void testReceiverExecute() {
        ConcreteCommand cmd = new ConcreteCommand(receiver);
        cmd.execute();
        //确保命令会调用执行者
        verify(receiver).action();

    }
}
