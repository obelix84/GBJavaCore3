package ru.gb.lesson6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;

public class TestNumberMethods {
    private NumbersMethods numbersMethods;

    @BeforeEach
    public void init(){
        numbersMethods = new NumbersMethods();
        System.out.println("init");
    }

    @Test
    public void testMethodOne1() {
        int [] numbers = {1,2,4,4,2,3,4,1,7};
        int [] exp = {1,7};
        Assertions.assertArrayEquals(exp, this.numbersMethods.methodOne(numbers));
    }

    @Test
    public void testMethodOne2() {
        int [] numbers = {1,2,4,4,2,3,4,1,4};
        int [] exp = {};
        Assertions.assertArrayEquals(exp, numbersMethods.methodOne(numbers));
    }

    @Test
    public void testMethodOne3() {
        int [] numbers = {1,2,4,4,2,3,44,1};
        int [] exp = {2,3,44,1};
        Assertions.assertArrayEquals(exp, numbersMethods.methodOne(numbers));
    }

    @Test
    public void testMethodOne4() {
        int [] numbers = {4,2,44,54,2,3,44,1};
        int [] exp = {2,44,54,2,3,44,1};
        Assertions.assertArrayEquals(exp, numbersMethods.methodOne(numbers));
    }

    @Test
    public void testMethodOne5() {
       int [] numbers = {0,2,44,54,2,3,44,1};
       Assertions.assertThrows(RuntimeException.class, ()-> {
           numbersMethods.methodOne(numbers);
       });
    }

    @Test
    public void testMethodTwo1() {
        int [] numbers = {1, 1, 1, 4, 4, 1, 4, 4};
        numbersMethods.methodTwo(numbers);
        Assertions.assertTrue(numbersMethods.methodTwo(numbers));
    }

}
