package org.cy;

import org.junit.*;

public class ChuangTestBefore {

    Chuang testObj; // 1. 声明一个全局的被测对象实例

    // ----------------------------------------------------
    // 2. 类级别生命周期方法 (@BeforeClass, @AfterClass)
    // 在所有测试用例执行之前/之后执行一次 (必须是 static)
    // ----------------------------------------------------
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("Run @BeforeClass: 在所有测试之前执行一次");
        // 这里通常用于创建连接、加载配置文件等昂贵操作
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("Run @AfterClass: 在所有测试之后执行一次");
        // 这里通常用于关闭连接、释放资源
    }

    // ----------------------------------------------------
    // 3. 用例级别生命周期方法 (@Before, @After)
    // 在每个 @Test 用例执行之前/之后执行一次
    // ----------------------------------------------------
    @Before // **核心：创建对象**
    public void setUp() throws Exception {
        testObj = new Chuang(0.0, 0.0); // 为每个测试创建新的对象
        System.out.println("Run @Before: 在每个测试用例之前执行");
    }

    @After // **核心：清理对象**
    public void tearDown() throws Exception {
        testObj = null; // 清理对象，确保没有残留影响
        System.out.println("Run @After: 在每个测试用例之后执行");
    }

    // ----------------------------------------------------
    // 4. 编写测试用例 (使用已创建的 testObj)
    // ----------------------------------------------------

    // 测试 '优秀'
    @Test
    public void testChuangGreat() {
        testObj.single_Max = 6.0;
        testObj.sum = 8.0;
        String expected = "优秀";
        // 使用 assertTrue(testObj.getGrade().equals(expected)); 或
        Assert.assertEquals(expected, testObj.getGrade());
    }

    // 测试 '不及格'
    @Test
    public void testChuangFail() {
        testObj.single_Max = 2.0;
        testObj.sum = 3.0;
        String expected = "不及格";
        Assert.assertEquals(expected, testObj.getGrade());
    }
}