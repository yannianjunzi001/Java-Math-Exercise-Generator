package org.cy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class) // 1. 指定使用参数化运行器
public class NextDayCalculatorTest {

    // 2. 定义字段以接收数据
    private int year;
    private int month;
    private int day;
    private String expectedNextDay;
    private NextDayCalculator calculator;

    // 3. 构造函数，用于注入数据
    public NextDayCalculatorTest(int year, int month, int day, String expectedNextDay) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.expectedNextDay = expectedNextDay;
        this.calculator = new NextDayCalculator();
    }

    // 4. 准备测试数据集（基于路径覆盖分析）
    @Parameters(name = "Test: {0}-{1}-{2} -> {3}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                // {Y, M, D}, {Expected}
                // 路径 1 & 2 (无效日期)
                {2001, 2, 29, "输入日期不正确"},
                {2000, 2, 30, "输入日期不正确"},
                // 路径 3 (平年, 非月末)
                {2001, 11, 29, "2001-11-30"},
                // 路径 4 (闰年, 非月末)
                {2000, 2, 28, "2000-2-29"},
                // 路径 5 (平年, 月末, 非12月)
                {2001, 1, 31, "2001-2-1"},
                // 路径 6 (闰年, 月末, 非12月)
                {2000, 2, 29, "2000-3-1"},
                // 路径 7 (平年, 月末, 12月)
                {2001, 12, 31, "2002-1-1"},
                // 路径 8 (闰年, 月末, 12月)
                {2000, 12, 31, "2001-1-1"}
        });
    }

    // 5. 编写测试方法（一个方法运行所有数据）
    @Test
    public void testGetNextDay_PathCoverage() {
        String actualNextDay = calculator.getNextDay(year, month, day);
        assertEquals(expectedNextDay, actualNextDay);
    }
}
