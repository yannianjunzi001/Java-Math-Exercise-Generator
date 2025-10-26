package org.cy;
 // 确保包名与 Chuang.java 匹配
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized; // 导入参数化运行器
import org.junit.runner.RunWith;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;

@RunWith(Parameterized.class) // 1. 指定参数化运行器
public class ChuangTestPara {

    // 2. 定义私有字段，用于接收构造函数注入的数据
    private double singleMax;
    private double sum;
    private String expected;
    private Chuang testObj; // 被测对象实例

    // 3. 定义构造函数，JUnit 将数据注入到此构造函数中
    public ChuangTestPara(double singlemax, double sum, String exp) {
        this.singleMax = singlemax;
        this.sum = sum;
        this.expected = exp;
    }

    // 4. 准备测试数据集
    @Parameterized.Parameters(name="{index}: getGrade({0},{1})={2}")
    public static Collection<Object[]> testDataset() {
        return Arrays.asList(new Object[][]{
                // {singleMax, sum, expected}
                {6.0, 8.0, "优秀"},  // 测试优秀
                {4.0, 6.0, "良好"},  // 测试良好
                {3.0, 5.0, "中等"},  // 测试中等
                {2.0, 4.0, "及格"},  // 测试及格
                {2.0, 3.0, "不及格"} // 测试不及格
        });
    }

    // 可选：使用 @Before 创建对象，或者直接在 @Test 中创建
     @Before
     public void setUp() {
         testObj = new Chuang(singleMax, sum);
     }

    // 5. 编写测试用例 (只需一个方法)
    @Test
    public void testGetGrade() {
        // 在这里创建对象，使用构造函数注入的参数
        Chuang testObj = new Chuang(singleMax, sum);

        // 核心：使用注入的数据进行断言，一个方法运行五次
        assertTrue(testObj.getGrade().equals(expected));
        // 或 assertEquals(expected, testObj.getGrade());
    }
}
