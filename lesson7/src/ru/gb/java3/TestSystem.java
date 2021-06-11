package ru.gb.java3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestSystem {

    public static void start(Class testClass) {
        List<Method> methods = new ArrayList<>();
        Method[] classMethods = testClass.getDeclaredMethods();

        //формируем массив методов
        for (Method classMethod : classMethods) {
            if (classMethod.isAnnotationPresent(Test.class)) {
                methods.add(classMethod);
            }
        }
        //сортируем по приоритету
        //static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor)
        methods.sort(Comparator.comparingInt((Method m) ->
                m.getAnnotation(Test.class).priority())
                .reversed());

        //BeforeSuite
        int countBS = 0;
        for (Method classMethod : classMethods) {
            if (classMethod.isAnnotationPresent(BeforeSuite.class)) {
                if (++countBS > 1) throw new RuntimeException("BeforeSuite может быть только один раз!");
                methods.add(0, classMethod);
            }
        }
        //BeforeSuite
        int countAS = 0;
        for (Method classMethod : classMethods) {
            if (classMethod.isAnnotationPresent(AfterSuite.class)) {
                if (++countAS > 1) throw new RuntimeException("AfterSuite может быть только один раз!");
                methods.add(classMethod);
            }
        }
        //выполняем методы
        for (Method m : methods) {
            try {
                m.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
