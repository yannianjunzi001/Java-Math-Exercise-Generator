package org.cy;

public class SalaryCalculator {
    /**
     * 计算员工工资。
     * @param hours 工作时间（小时）
     * @param wagePerHr 每小时的基本工资 (g)
     * @return 计算后的总工资
     * @throws IllegalArgumentException 如果输入时间无效（<0 或 >60）
     */
    public double calculateSalary(double hours, double wagePerHr) throws IllegalArgumentException {
        double salary;

        // 1. 无效输入处理
        if (hours < 0 || hours > 60) {
            throw new IllegalArgumentException("输入的工作时间无效！必须在 0 到 60 小时之间。");
        }

        // 2. 正常工作时间 (h <= 40)
        if (hours <= 40) {
            if (hours == 40) {
                // h = 40 小时，按原工资计算 (40 * g)
                salary = 40 * wagePerHr;
            } else {
                // h < 40 小时，按 0.7 倍工资计算 (h * 0.7 * g)
                salary = hours * 0.7 * wagePerHr;
            }
        }
        // 3. 略微加班 (40 < h <= 50)
        else if (hours <= 50) {
            // 40小时按原工资g，超出部分按 1.5倍 g 计算
            salary = 40 * wagePerHr + (hours - 40) * 1.5 * wagePerHr;
        }
        // 4. 大幅加班 (50 < h <= 60)
        else { // hours > 50 && hours <= 60
            // 40小时g + 10小时 1.5g + 超出50的部分 3g
            salary = 40 * wagePerHr + 10 * 1.5 * wagePerHr + (hours - 50) * 3 * wagePerHr;
        }

        return salary;
    }
}
