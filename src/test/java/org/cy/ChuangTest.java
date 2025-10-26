package org.cy;

import org.junit.Test;
import static org.junit.Assert.*;

public class ChuangTest {
    @Test
    public void testExcellentGrade() {
        Chuang testObj = new Chuang(5.0, 7.0); // 最小优秀分数
        assertEquals("优秀", testObj.getGrade());
    }

    // 2. 测试 "良好" 边界 (single_Max >= 4 && sum >= 6)
    @Test
    public void testGoodGrade() {
        Chuang testObj = new Chuang(4.0, 6.0); // 最小良好分数
        assertEquals("良好", testObj.getGrade());

        // 测试单项未达标，但总分达标的情况（应是次级评定）
        Chuang testObjFail = new Chuang(3.9, 7.0);
        // 7分总分但单项不达标，会进入下一个分支，但这个逻辑需要根据实际代码判断。
        // 假设按代码逻辑走，它会继续向下判断。
        assertNotEquals("优秀", testObjFail.getGrade());
    }

    // 3. 测试 "中等" 边界 (single_Max >= 3 && sum >= 5)
    @Test
    public void testAverageGrade() {
        Chuang testObj = new Chuang(3.0, 5.0); // 最小中等分数
        assertEquals("中等", testObj.getGrade());
    }

    // 4. 测试 "及格" (sum >= 4)
    @Test
    public void testPassGrade() {
        // 此时单项和总分均低于中等要求，只看总分
        Chuang testObj = new Chuang(2.0, 4.0);
        assertEquals("及格", testObj.getGrade());
    }

    // 5. 测试 "不及格" (sum < 4)
    @Test
    public void testFailGrade() {
        Chuang testObj = new Chuang(1.0, 3.9);
        assertEquals("不及格", testObj.getGrade());
    }
}
