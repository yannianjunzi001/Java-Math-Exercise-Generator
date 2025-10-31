package org.cy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

// 注意：这里使用参数化测试来运行边界值用例（结合了实验五的学习成果）

@RunWith(Parameterized.class)
public class NextDayCalculatorFunctionalTest {

    // 1. 被测类（假设已修复实验五中的BUG）
    private NextDayCalculator calculator = new NextDayCalculator();

    // 2. 参数定义
    private int year;
    private int month;
    private int day;
    private String expected;

    // 3. 构造函数：用于JUnit传入参数
    public NextDayCalculatorFunctionalTest(int year, int month, int day, String expected) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.expected = expected;
    }

    // 4. 数据集：包含等价类和边界值测试用例
    @Parameterized.Parameters(name = "{0}-{1}-{2} -> {3}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
                // --- 边界值/判定表测试用例 (Functional Testing) ---

                // B1: 最小年份
                {1900, 1, 1, "1900-1-2"},
                // B2: 最大年份，跨年
                {2050, 12, 31, "2051-1-1"},
                // B3: 跨月 (31天月边界)
                {2001, 1, 31, "2001-2-1"},
                // B4: 跨月 (30天月边界)
                {2001, 4, 30, "2001-5-1"},
                // B5: 跨月 (平年2月边界)
                {2001, 2, 28, "2001-3-1"},
                // B6: 跨月 (闰年2月边界)
                {2004, 2, 29, "2004-3-1"},
                // B7: 闰年非月末
                {2000, 2, 28, "2000-2-29"},
                // B8: 跨年边界
                {2001, 12, 31, "2002-1-1"},

                // R1: 月中日期（判定表/等价类）
                {2001, 1, 15, "2001-1-16"},

                // --- 无效等价类测试用例 (Invalid) ---
                // B9: 日期超范围 (大)
                {2001, 1, 32, "输入日期不正确"},
                // B10: 月份超范围 (大)
                {2001, 13, 1, "输入日期不正确"},
                // B11: 无效输入（平年二月 30） (R4)
                {2001, 2, 30, "输入日期不正确"},
                // B12: 无效输入（小月 31）
                {2001, 4, 31, "输入日期不正确"},
                // IEC1: 低于最小年份
                {1899, 1, 1, "输入日期不正确"}
        });
    }

    // 5. 测试方法：统一执行断言
    @Test
    public void testGetNextDayWithFunctionalCases() {
        String actual = calculator.getNextDay(year, month, day);
        assertEquals(expected, actual);
    }
}
