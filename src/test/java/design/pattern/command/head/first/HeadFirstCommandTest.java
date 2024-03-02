package design.pattern.command.head.first;

import design.pattern.command.head.first.command.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 采用命令模式开启Party模式
 * 一个命令：打开厨房灯，卧室灯，打开电风扇且电风扇给最大风，打开CD
 */
public class HeadFirstCommandTest {
    /**
     * 灯光
     */
    Light kitchenLight = new Light("厨房");
    Light livingRoomLight = new Light("卧室");
    /**
     * 电风扇
     */
    CeilingFan ceilingFan = new CeilingFan();
    /**
     * 立体声音响
     */
    StereoWithCD stereoWithCD = new StereoWithCD();

    Command kitchenLightOnCommand = new LightOnCommand(kitchenLight);
    Command kitchenLightOffCommand = new LightOffCommand(kitchenLight);
    Command livingRoomLightOnCommand = new LightOnCommand(livingRoomLight);
    Command livingRoomLightOffCommand = new LightOffCommand(livingRoomLight);
    StereoWithCDOnCommand stereoWithCDCommand = new StereoWithCDOnCommand(stereoWithCD, 11);
    CeilingFanHighOnCommand ceilingFanCommand = new CeilingFanHighOnCommand(ceilingFan);

    /**
     * 开party
     */
    @Test
    public void testParty() {
        List<Command> commands = Arrays.asList(kitchenLightOnCommand, livingRoomLightOnCommand, stereoWithCDCommand, ceilingFanCommand);
        Command partyCommand = new PartyCommand(commands);
        assertEquals("厨房灯开启,卧室灯开启,音响开启,电风扇开启,风速:3", partyCommand.execute());
        assertEquals("电风扇关闭,音响关闭,卧室灯关闭,厨房灯关闭", partyCommand.undo());
    }

    /**
     * 测试命令可以被正常执行
     */
    @Test
    public void testCommandInvoker() {
        Invoker invoker = new Invoker();
        //默认空命令
        assertEquals("", invoker.invoke());
        invoker.setCommand(kitchenLightOnCommand);
        assertEquals("厨房灯开启", invoker.invoke());
        assertEquals("厨房灯关闭", invoker.undo());

    }

    /**
     * 测试音响命令
     */
    @Test
    public void testStereoWithCDCommand() {
        assertEquals("音响开启", stereoWithCDCommand.execute());
        assertEquals("音响关闭", stereoWithCDCommand.undo());
    }

    /**
     * 测试电风扇命令
     */
    @Test
    public void testCeilingFanCommand() {
        assertEquals("电风扇开启,风速:3", ceilingFanCommand.execute());
        assertEquals("电风扇关闭", ceilingFanCommand.undo());

    }

    /**
     * 测试好灯的命令都是好的
     */
    @Test
    public void testLightCommand() {
        assertEquals("厨房灯开启", kitchenLightOnCommand.execute());
        assertEquals("厨房灯开启", kitchenLightOffCommand.undo());
        assertEquals("卧室灯关闭", livingRoomLightOffCommand.execute());
        assertEquals("卧室灯关闭", livingRoomLightOnCommand.undo());
    }

    /**
     * 准备好立体音响
     */
    @Test
    public void testStereoWithCD() {
        assertEquals("音响开启", stereoWithCD.on());
        assertEquals("放入CD", stereoWithCD.setCD());
        assertEquals("调节音量为11", stereoWithCD.setVolume(11));


    }

    /**
     * 测试电风扇开关
     */
    @Test
    public void testCeilingFan() {
        assertEquals("电风扇开启", ceilingFan.on());
        ceilingFan.low();
        assertEquals("风速:1", ceilingFan.getSpeed());
        ceilingFan.medium();
        assertEquals("风速:2", ceilingFan.getSpeed());
        ceilingFan.high();
        assertEquals("风速:3", ceilingFan.getSpeed());
        assertEquals("电风扇关闭", ceilingFan.off());

    }

    /**
     * 测试灯可以开关
     */
    @Test
    public void testLight() {
        assertEquals("厨房灯开启", kitchenLight.on());
        assertEquals("卧室灯关闭", livingRoomLight.off());
    }
}
