package org.cy;

public class Chuang {
    public double single_Max; // 单项最高分
    public double sum;        // 总分

    // 构造函数
    public Chuang(double sm, double s) {
        this.single_Max = sm;
        this.sum = s;
    }

    // 核心方法：根据规则评定成绩
    public String getGrade() {
        String result = "";

        if (single_Max >= 5 && sum >= 7) {
            result = "优秀";
        } else if (single_Max >= 4 && sum >= 6) {
            result = "良好";
        } else if (single_Max >= 3 && sum >= 5) {
            result = "中等";
        } else if (sum >= 4) {
            result = "及格";
        } else if (sum < 4) {
            result = "不及格";
        }

        return result;
    }

    // 简化的 Getter/Setter，如果需要
    // public double getSingleMax() { return single_Max; }
    // public double getSum() { return sum; }
}
