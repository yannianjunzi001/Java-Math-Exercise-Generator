package org.cy; // 必须和主类使用相同的包名

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * AdvancedMathExerciseGenerator 的 JUnit 4 测试类
 * 重点测试核心功能：生成题目、结果和中间结果的约束检查。
 */
public class AdvancedMathExerciseGeneratorTest {

    // --- 测试常量和约束 ---
    private static final int MIN = 1;
    private static final int MAX = 100; // 约束要求 (0, 100)，即 [1, 99]

    // 辅助方法：调用主类的生成方法，以在测试中重用逻辑
    private static AdvancedMathExerciseGenerator.Exercise generateExerciseForTest(int operationType, int numCount) {
        // 增加尝试次数以提高成功率，避免假失败
        for (int i = 0; i < 500; i++) {
            // 由于在同一个包 org.cy 下，可以访问 package-private 的 generateValidExercise 方法
            AdvancedMathExerciseGenerator.Exercise exercise =
                    AdvancedMathExerciseGenerator.generateValidExercise(operationType, numCount);
            if (exercise != null) {
                return exercise;
            }
        }
        // 100次尝试失败改为 500次，如果仍然失败，则说明生成逻辑有问题
        fail("未能生成满足约束的题目 (500次尝试失败)");
        return null;
    }


    // --- 核心约束测试 ---

    @Test
    public void testFinalResultConstraint() {
        // 测试：最终结果约束 [1, 99]
        for (int i = 0; i < 100; i++) {
            Random rand = new Random();
            int type = rand.nextInt(5) + 1;
            int count = rand.nextBoolean() ? 2 : 3;

            AdvancedMathExerciseGenerator.Exercise exercise = generateExerciseForTest(type, count);
            int result = exercise.getIntValue();

            // 确保结果 >= 1 且 < 100
            assertTrue("最终结果 " + result + " 超出范围 [1, 99] for: " + exercise.getProblem(),
                    result >= MIN && result < MAX); // <--- 这里是修复失败的断言
        }
    }

    @Test
    public void testTwoNumberSub() {
        // 测试：两数纯减法结果为正
        for (int i = 0; i < 50; i++) {
            // 确保我们尝试生成纯减法（例如：类型 1，两个数字）
            AdvancedMathExerciseGenerator.Exercise exercise = generateExerciseForTest(1, 2);
            String problem = exercise.getProblem();

            // 只有当题目是减法时才校验
            if (problem.contains(" - ")) {
                assertTrue("纯减法结果应为正数", exercise.getIntValue() > 0);
            }
        }
    }

    @Test
    public void testTwoNumberDiv() {
        // 测试：两数纯除法必须是整数
        for (int i = 0; i < 50; i++) {
            // 确保我们尝试生成纯除法
            AdvancedMathExerciseGenerator.Exercise exercise = generateExerciseForTest(2, 2);
            String problem = exercise.getProblem();

            if (problem.contains(" / ")) {
                // 解析算式
                String[] parts = problem.split(" / ");
                int a = Integer.parseInt(parts[0].trim());
                String bStr = parts[1].split(" = ")[0].trim();
                int b = Integer.parseInt(bStr);

                // 检查是否整除
                assertEquals("纯除法运算要求能整除", 0, a % b);
            }
        }
    }

    @Test
    public void testBoundaryResults() {
        // 测试：结果不等于 0 或 100 (冗余检查，但用于边界测试)
        for (int i = 0; i < 100; i++) {
            // 尝试四则混合运算，覆盖面广
            AdvancedMathExerciseGenerator.Exercise exercise = generateExerciseForTest(5, 3);
            int result = exercise.getIntValue();

            assertNotEquals("结果不应为 0", 0, result);
            assertNotEquals("结果不应为 100", 100, result); // <--- 修复结果为 100 的断言
        }
    }

    @Test
    public void testThreeNumberGeneration() {
        // 测试：三数运算生成无异常
        for (int i = 0; i < 50; i++) {
            Random rand = new Random();
            int type = rand.nextInt(5) + 1;

            AdvancedMathExerciseGenerator.Exercise exercise = generateExerciseForTest(type, 3);

            // 确保能成功生成
            assertNotNull("三数运算生成失败", exercise);

            // 简单校验表达式格式： a op b op c = ?，至少有 7 个部分
            assertTrue("三数运算表达式格式错误", exercise.getProblem().split(" ").length >= 7);
        }
    }
}
