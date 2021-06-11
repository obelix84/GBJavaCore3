package ru.gb.java3;

public class TestClass {

    @AfterSuite
    public static void after() {
        System.out.println("After");
    }

    @Test
    public static void test1() {
        System.out.println("test1");
    }

    @Test(priority = 2)
    public static void test2() {
        System.out.println("test2");
    }

    @Test(priority = 10)
    public static void test3() {
        System.out.println("test3");
    }

    @Test(priority = 3)
    public static void test4() {
        System.out.println("test4");
    }

    @BeforeSuite
    public static void before() {
        System.out.println("Before");
    }
}
