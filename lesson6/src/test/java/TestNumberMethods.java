import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import ru.gb.lesson6.NumbersMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TestNumberMethods {
    private NumbersMethods numbersMethods;

    @BeforeEach
    public void init(){
        numbersMethods = new NumbersMethods();
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

    // Для пробы, хочу посмотреть как работает
    @ParameterizedTest
    @MethodSource("dataForMethodOne")
    public void testMethodOneAll(int[] array, int [] result) {
        Assertions.assertArrayEquals(result, numbersMethods.methodOne(array));
    }

    public static Stream<Arguments> dataForMethodOne() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[] {1,2,4,4,2,3,4,1,7}, new int[] {1,7}));
        out.add(Arguments.arguments(new int[] {1,2,4,4,2,3,4,1,4}, new int[] {}));
        out.add(Arguments.arguments(new int[] {1,2,4,4,2,3,44,1}, new int[] {2,3,44,1}));
        out.add(Arguments.arguments(new int[] {4,2,44,54,2,3,44,1}, new int[] {2,44,54,2,3,44,1}));
        return out.stream();
    }


    @Test
    public void testMethodTwo1() {
        int [] numbers = {1, 1, 1, 4, 4, 1, 4, 4};
        Assertions.assertTrue(numbersMethods.methodTwo(numbers));
    }

    @Test
    public void testMethodTwo2() {
        int [] numbers = {1, 1, 1, 3, 4, 1, 4, 4};
        Assertions.assertFalse(numbersMethods.methodTwo(numbers));
    }

    @Test
    public void testMethodTwo3() {
        int [] numbers = {1, 1, 1, 1, 1, 1};
        Assertions.assertFalse(numbersMethods.methodTwo(numbers));
    }

    @Test
    public void testMethodTwo4() {
        int [] numbers = {4, 4, 4, 4, 4, 4, 4, 4};
        Assertions.assertFalse(numbersMethods.methodTwo(numbers));
    }

}
