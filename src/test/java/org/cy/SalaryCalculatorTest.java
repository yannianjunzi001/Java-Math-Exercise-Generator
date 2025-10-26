package org.cy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SalaryCalculatorTest {

    // 假设基本时薪 g = 10 元/小时
    private static final double WAGE_G = 10.0;

    private double hours;
    private double expectedSalary;
    private boolean isExceptionExpected; // 是否预期抛出异常

    // 构造函数用于注入数据
    public SalaryCalculatorTest(double hours, double expectedSalary, boolean isExceptionExpected) {
        this.hours = hours;
        this.expectedSalary = expectedSalary;
        this.isExceptionExpected = isExceptionExpected;
    }

    // 定义所有测试数据（结合等价类和边界值）
    @Parameters(name = "Hours:{0}, Expected:{1}")
    public static Collection<Object[]> testData() {
        // 注意：对于预期结果，我们将计算结果代入，以消除表格中的代数表达式
        return Arrays.asList(new Object[][]{
                // *** 等价类测试 (图 5-14) ***
                // {hours, expectedSalary, isExceptionExpected}
                {20.0, 20 * 0.7 * WAGE_G, false}, // 1. 0 < h < 40 (20 * 0.7 * 10 = 140)
                {45.0, 40 * WAGE_G + 5 * 1.5 * WAGE_G, false}, // 2. 40 < h <= 50 (400 + 75 = 475)
                {55.0, 40 * WAGE_G + 10 * 1.5 * WAGE_G + 5 * 3 * WAGE_G, false}, // 3. 50 < h <= 60 (400 + 150 + 150 = 700)
                {-10.0, 0.0, true}, // 4. 无效输入 (<0)
                {70.0, 0.0, true}, // 5. 无效输入 (>60)

                // *** 边界值测试 (图 5-15) ***
                // {hours, expectedSalary, isExceptionExpected}
                {-1.0, 0.0, true},    // 1. -1 (0的边界外)
                {0.0, 0.0 * 0.7 * WAGE_G, false}, // 2. 0 (0的边界)
                {39.0, 39 * 0.7 * WAGE_G, false}, // 3. 39 (40的边界内)
                {40.0, 40 * WAGE_G, false},       // 4. 40 (40的边界)
                {41.0, 40 * WAGE_G + 1 * 1.5 * WAGE_G, false}, // 5. 41 (40的边界外)
                {49.0, 40 * WAGE_G + 9 * 1.5 * WAGE_G, false}, // 6. 49 (50的边界内)
                {50.0, 40 * WAGE_G + 10 * 1.5 * WAGE_G, false}, // 7. 50 (50的边界)
                {51.0, 40 * WAGE_G + 10 * 1.5 * WAGE_G + 1 * 3 * WAGE_G, false}, // 8. 51 (50的边界外)
                {59.0, 40 * WAGE_G + 10 * 1.5 * WAGE_G + 9 * 3 * WAGE_G, false}, // 9. 59 (60的边界内)
                {60.0, 40 * WAGE_G + 10 * 1.5 * WAGE_G + 10 * 3 * WAGE_G, false}, // 10. 60 (60的边界)
                {61.0, 0.0, true}     // 11. 61 (60的边界外)
        });
    }

    @Test
    public void testCalculateSalary() {
        SalaryCalculator cal = new SalaryCalculator();

        if (isExceptionExpected) {
            // 如果预期抛出异常（hours < 0 或 hours > 60）
            try {
                cal.calculateSalary(hours, WAGE_G);
                // 如果代码执行到这里，说明应该抛异常但没抛，测试失败
                fail("预期应该抛出 IllegalArgumentException，但未抛出。");
            } catch (IllegalArgumentException e) {
                // 捕获到预期的异常，测试通过
                assertTrue(true);
            }
        } else {
            // 如果预期正常结果，则直接断言
            double actualSalary = cal.calculateSalary(hours, WAGE_G);
            // 使用 delta 0.0001 比较浮点数
            assertEquals(expectedSalary, actualSalary, 0.0001);
        }
    }
}
