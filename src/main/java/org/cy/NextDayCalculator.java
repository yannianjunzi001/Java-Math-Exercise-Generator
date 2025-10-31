package org.cy;

public class NextDayCalculator {

    /**
     * 实验五提供的原始（有缺陷的）getNextDay 方法
     */
    public String getNextDay(int year, int month, int day) {
        if (year < 1900 || year > 2050) {
            return "输入日期不正确";
        }
        // 闰年、平年每月的天数
        int[][] days = {
                {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}, // index=0: 平年
                {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}  // index=1: 闰年
        };

        int index = 0;
        // *** BUG 1: 闰年判断逻辑错误 ***
        // 1900年会被错误地判断为闰年。正确的应为:
        // if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) { // <--- 修正 BUG 1
            index = 1;
        }

        // 判断输入日期是否正确
        if (month < 1 || month > 12 || day < 1 || day > days[index][month - 1]) {
            return "输入日期不正确";
        }

        // 小于当前月份的天数
        if (day < days[index][month - 1]) {
            day++;
        }
        // 等于当前月份的天数（月末）
        else {
            if (month == 12) {
                year++;
                month = 1;
                day = 1; // <--- 修正 BUG 2
            } else {
                month++;
                day = 1;
            }
        }
        return year + "-" + month + "-" + day;
    }
}
