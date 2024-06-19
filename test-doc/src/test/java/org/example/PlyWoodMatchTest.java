package org.example;

import junit.framework.TestCase;
import org.junit.Test;

public class PlyWoodMatchTest extends TestCase {
    /**
     * 被测试类
     */
    PlyWoodMatch plyWoodMatch = new PlyWoodMatch();

    /**
     * 测试配对日
     * 目的:
     * 测试胶合板每日选择交割正常配对
     * 期望结果:
     * 给定买卖方都正常配对
     */
    @Test
    public void testMatch01() {
    }

    /**
     * 测试配对日02
     * 目的:
     * 测试胶合板每日选择交割被动选择配对
     * 期望结果:
     * 被动选择的买方都正常配对
     */
    @Test
    public void testMatch02() {
    }

    /**
     * 测试配对日存储
     * 目的:
     * 测试配对结果正常存储
     * 期望结果:
     * 存储结果都正确
     */
    @Test
    public void testSaveMatch() {
    }

    void none() {

    }
}