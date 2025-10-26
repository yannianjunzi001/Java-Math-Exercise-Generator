package org.cy; // 确保您的包名与文件路径匹配

import org.junit.Test;
import static org.junit.Assert.*; // 静态导入，简化 assertEquals 写法

public class CalculatorTest {

    @Test
    public void testAdd() {
        Calculator cal = new Calculator();
        // 期望 1 + 2 = 3
        assertEquals(3, cal.add(1, 2)); // 注意：现在可以直接写 assertEquals
    }
    @Test
    public void testDivd() throws Exception {
        Calculator cal = new Calculator();
        // 期望 4/1 = 4
        // 注意：Calculator 类的 divd 方法是错误的 (a-b)，所以测试会失败！
        assertEquals(4, cal.divd(4, 1));
    }
}