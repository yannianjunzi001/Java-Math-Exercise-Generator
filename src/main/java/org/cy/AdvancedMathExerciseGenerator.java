package org.cy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * 实验二：四则混合运算自动出题程序
 * 增加了：乘除法、三数运算、运算数据和中间结果的范围控制（0 < x < 100, 整数）、
 * 输出到 CSV 文件、题目数量可控。
 */
public class AdvancedMathExerciseGenerator {

    private static final Random random = new Random();
    private static final int MAX_VALUE = 100; // 最大值边界（不包含 100，实际结果上限是 99）
    private static final int MIN_VALUE = 1; // 最小值边界（包含 1）

    // --- 主函数：用户交互与流程控制 ---
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. 选择运算类型和数字数量
        System.out.println("请选择运算类型组合（1-3个数字）：");
        System.out.println("1. 纯加法 / 纯减法 (2-3数)");
        System.out.println("2. 纯乘法 / 纯除法 (2-3数)");
        System.out.println("3. 加减法混合运算 (2-3数)");
        System.out.println("4. 乘除法混合运算 (2-3数)");
        System.out.println("5. 四则混合运算 (2-3数)");
        int operationType = getIntInput(scanner, "请输入您的选择 (1-5)：", 1, 5);

        System.out.print("请选择参与运算的数字个数（2 或 3）：");
        int numCount = getIntInput(scanner, "请输入您的选择 (2 或 3)：", 2, 3);

        // 2. 设置题目数量和输出文件名
        System.out.printf("请输入要生成的题目数量 (如 30)：");
        int count = getIntInput(scanner, "请输入题目数量：", 1, 1000); // 假设最大1000道

        System.out.print("请输入输出文件名（如 exercises.csv）：");
        String filename = scanner.next();

        // 3. 生成并输出题目
        try {
            generateAndSaveExercises(operationType, numCount, count, filename);
            System.out.printf("\n成功生成 %d 道题目，并保存到文件：%s\n", count, filename);
        } catch (IOException e) {
            System.err.println("文件写入失败：" + e.getMessage());
        }

        scanner.close();
    }

    // --- 核心方法：生成并保存题目 ---
    /**
     * 生成题目并保存到 CSV 文件
     */
    private static void generateAndSaveExercises(int operationType, int numCount, int count, String filename)
            throws IOException {

        try (FileWriter writer = new FileWriter(filename)) {
            // CSV 文件头
            writer.write("序号,题目,答案\n");

            int exercisesPerRow = 5;
            for (int i = 0; i < count; i++) {
                // 循环生成题目直到满足所有约束
                Exercise exercise;
                do {
                    // 使用 package-private 方法
                    exercise = generateValidExercise(operationType, numCount);
                } while (exercise == null);

                // 写入 CSV
                String csvLine = String.format("%d,%s,%s", i + 1, exercise.getProblem(), exercise.getAnswer());
                writer.write(csvLine);

                // 格式化输出到控制台（5列展示）
                System.out.printf("%d.%s = ?\t\t", i + 1, exercise.getProblem());

                if ((i + 1) % exercisesPerRow == 0 || (i + 1) == count) {
                    writer.write("\n"); // 换行
                    System.out.println(); // 换行
                } else {
                    writer.write(","); // 同行题目之间用逗号分隔
                }
            }
        }
    }

    /**
     * 循环生成一道满足所有约束的题目
     * 已从 private 修改为 package-private，以便在测试中调用。
     */
    static Exercise generateValidExercise(int operationType, int numCount) {
        // 尝试生成题目，直到满足所有约束
        for (int attempt = 0; attempt < 1000; attempt++) {
            try {
                return generateExercise(operationType, numCount);
            } catch (InvalidExerciseException e) {
                // 不满足约束，继续下一次尝试
            }
        }
        return null; // 1000次尝试失败，返回null
    }

    // --- 核心算法：生成满足约束的算式 ---

    /**
     * 生成一道练习题（可能抛出异常）
     */
    private static Exercise generateExercise(int operationType, int numCount) throws InvalidExerciseException {
        if (numCount == 2) {
            return generateTwoNumberExercise(operationType);
        } else { // numCount == 3
            return generateThreeNumberExercise(operationType);
        }
    }

    /**
     * 生成两数运算
     */
    private static Exercise generateTwoNumberExercise(int type) throws InvalidExerciseException {
        // 生成两个 1-99 的整数
        int a = generateRandomNumber();
        int b = generateRandomNumber();
        char operator = selectOperator(type, false); // false: 不包含乘除

        // 确保减法和除法满足约束
        if (operator == '-') {
            // 确保结果在 [1, 99] 之间
            if (a <= b) {
                // 调整 a, b 以保证 a > b
                if (a == MAX_VALUE - 1 && b == MIN_VALUE) {
                    // 难以满足 a-b > 0 且在范围内，跳过
                }
                int temp = a; a = b; b = temp; // 交换以保证 a > b
            }
        } else if (operator == '/') {
            // 确保 a 能被 b 整除，且结果在 [1, 99]
            b = ensureDivisor(a, MAX_VALUE - 1);
        }

        int result = calculate(a, b, operator);
        // *** 关键修复点：最终结果必须小于 100 (即 <= 99) ***
        if (result < MIN_VALUE || result >= MAX_VALUE) {
            throw new InvalidExerciseException("Result out of range [1, 99]");
        }

        String problem = String.format("%d %c %d", a, operator, b);
        return new Exercise(problem, result);
    }

    /**
     * 生成三数运算
     */
    private static Exercise generateThreeNumberExercise(int type) throws InvalidExerciseException {
        // 生成三个 1-99 的整数
        int a = generateRandomNumber();
        int b = generateRandomNumber();
        int c = generateRandomNumber();

        // 随机选择两个运算符
        char op1 = selectOperator(type, true);
        char op2 = selectOperator(type, true);

        String problem;
        int result;

        // 1. 确保第一个子表达式 (a op1 b) 满足约束
        int intermediateResult;

        if (op1 == '-') {
            if (a <= b) { int temp = a; a = b; b = temp; } // 保证 a > b
        } else if (op1 == '/') {
            b = ensureDivisor(a, MAX_VALUE - 1); // 除数调整
        }

        intermediateResult = calculate(a, b, op1);
        // *** 关键修复点：中间结果必须小于 100 (即 <= 99) ***
        if (intermediateResult < MIN_VALUE || intermediateResult >= MAX_VALUE) {
            throw new InvalidExerciseException("Intermediate result 1 out of range [1, 99]");
        }

        // 2. 确保第二个子表达式 (intermediateResult op2 c) 满足约束
        int secondOperand = c;
        if (op2 == '-') {
            if (intermediateResult <= secondOperand) {
                // 如果中间结果太小 (<= 1)，则无法进行减法
                if (intermediateResult <= MIN_VALUE) {
                    throw new InvalidExerciseException("Intermediate result too small for further subtraction");
                }

                // 保证 intermediateResult > secondOperand，且 secondOperand >= MIN_VALUE
                // maxBound = intermediateResult - MIN_VALUE (确保 bound > 0)
                int maxBound = intermediateResult - MIN_VALUE;

                // secondOperand 范围是 [MIN_VALUE, intermediateResult - 1]
                secondOperand = random.nextInt(maxBound) + MIN_VALUE;
            }
        } else if (op2 == '/') {
            secondOperand = ensureDivisor(intermediateResult, MAX_VALUE - 1); // 除数调整
        }

        result = calculate(intermediateResult, secondOperand, op2);
        // *** 关键修复点：最终结果必须小于 100 (即 <= 99) ***
        if (result < MIN_VALUE || result >= MAX_VALUE) {
            throw new InvalidExerciseException("Final result out of range [1, 99]");
        }

        problem = String.format("%d %c %d %c %d", a, op1, b, op2, secondOperand);
        return new Exercise(problem, result);
    }

    // --- 辅助工具方法 ---

    /**
     * 获取 1 到 MAX_VALUE-1 (99) 之间的随机数
     */
    private static int generateRandomNumber() {
        return random.nextInt(MAX_VALUE - MIN_VALUE) + MIN_VALUE;
    }

    /**
     * 根据类型选择运算符
     */
    private static char selectOperator(int type, boolean allowAll) {
        String operators = "";
        switch (type) {
            case 1: operators = "+-"; break; // 纯加/减
            case 2: operators = "*/"; break; // 纯乘/除
            case 3: operators = "+-"; break; // 加减混合
            case 4: operators = "*/"; break; // 乘除混合
            case 5: operators = allowAll ? "+-*/" : "+-"; break; // 四则混合 (这里简化为两数运算只用加减，三数运算用四则)
        }
        if(operators.isEmpty()) operators = "+"; // 默认加法

        return operators.charAt(random.nextInt(operators.length()));
    }

    /**
     * 计算两数运算结果
     */
    private static int calculate(int a, int b, char operator) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
            default: throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    /**
     * 确保 a 能被返回的 b 整除，且 b 在 [1, maxDivisor] 范围内
     */
    private static int ensureDivisor(int a, int maxDivisor) throws InvalidExerciseException {
        // 寻找 a 的所有因子
        java.util.List<Integer> divisors = new java.util.ArrayList<>();
        // 从 MIN_VALUE (1) 开始找因子
        for (int i = MIN_VALUE; i <= maxDivisor; i++) {
            // 确保因子本身也在范围内
            if (a % i == 0 && i < MAX_VALUE) {
                divisors.add(i);
            }
        }

        if (divisors.isEmpty()) {
            throw new InvalidExerciseException("No valid divisor found for " + a);
        }

        // 随机选择一个因子作为除数
        return divisors.get(random.nextInt(divisors.size()));
    }

    /**
     * 获取用户输入的整数，并进行范围校验
     */
    private static int getIntInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.printf("输入无效，请输入 %d 到 %d 之间的整数。\n", min, max);
                }
            } else {
                System.out.println("输入无效，请输入一个整数。");
                scanner.next(); // 消耗掉错误的输入
            }
        }
    }

    // --- 数据结构 ---

    /**
     * 用于封装生成的习题
     */
    public static class Exercise {
        private final String problem;
        private final int answer;

        public Exercise(String problem, int answer) {
            this.problem = problem;
            this.answer = answer;
        }

        public String getProblem() { return problem + " = ?"; }
        public String getAnswer() { return String.valueOf(answer); }
        public int getIntValue() { return answer; } // 方便测试
    }

    /**
     * 自定义异常，用于控制生成过程
     */
    public static class InvalidExerciseException extends Exception {
        public InvalidExerciseException(String message) {
            super(message);
        }
    }
}
