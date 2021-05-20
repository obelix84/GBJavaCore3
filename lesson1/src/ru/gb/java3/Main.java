package ru.gb.java3;

import com.sun.org.apache.xpath.internal.operations.Or;
import ru.gb.java3.fruits.Apple;
import ru.gb.java3.fruits.Orange;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        doTask1();
        doTask2();
        doTask3();

    }

    public static void doTask1() {
        String [] strArr = {"первый", "второй", "третий", "четвертый"};
        Integer [] intArr = new Integer[10];
        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = i;
        }

        printGenArr(strArr);
        if (swap(strArr, 1, 2)) {
            printGenArr(strArr);
        } else {
            System.out.println("Что-то пошло не так!");
        }

        printGenArr(intArr);
        if (swap(intArr, 1, 9)) {
            printGenArr(intArr);
        } else {
            System.out.println("Что-то пошло не так!");
        }

        printGenArr(intArr);
        if (swap(intArr, 11, 11)) {
            printGenArr(intArr);
        } else {
            System.out.println("Что-то пошло не так!");
        }

    }

    //вспомогательный метод, выводит любой массив на экран
    public static <T> void printGenArr (T [] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
    //Написать метод, который меняет два элемента массива местами (массив может быть любого ссылочного типа);
    //Возвращает false, если не удалось поменять местами элементы
    public static <T> boolean swap (T[] arr, int i, int j) {
        if (i >= arr.length || j >= arr.length) {
            System.out.println("Индекс i или j за границами массива!");
            return false;
        }
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return true;
    }

    //Написать метод, который преобразует массив в ArrayList;
    public static void doTask2() {
        String [] strArr = {"первый", "второй", "третий", "четвертый"};
        Integer [] intArr = new Integer[10];
        for (int i = 0; i < intArr.length; i++) {
            intArr[i] = i;
        }

        int [] simpleIntArr = new int[10];
        for (int i = 0; i < simpleIntArr.length; i++) {
            simpleIntArr[i] = i;
        }

        List<String> list1 = toList(strArr);
        System.out.println(list1.toString());
        List<Integer> list2 = toList(intArr);
        System.out.println(list2.toString());
    }

    public static <T> List<T> toList(T [] arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }

    public static void doTask3() {
        System.out.println("первая коробка яблок");

        Apple[] appleArr1 = {
                new Apple(),
                new Apple(),
                new Apple()
        };
        Apple oneApple = new Apple();

        Box<Apple> appleBox1 = new Box<>();
        System.out.println(appleBox1.getWeight());
        appleBox1.addSeveral(appleArr1);
        System.out.println(appleBox1.getWeight());
        appleBox1.add(oneApple);
        System.out.println(appleBox1.getWeight());

        System.out.println("вторая коробка яблок");

        Apple[] appleArr2 = {
                new Apple(),
                new Apple(),
                new Apple(),
                new Apple(),
                new Apple(),
                new Apple()
        };
        Apple secondApple = new Apple();

        Box<Apple> appleBox2 = new Box<>(appleArr2);
        System.out.println(appleBox2.getWeight());
        //appleBox2.addSeveral(appleArr2);
        System.out.println(appleBox2.getWeight());
        //appleBox2.add(secondApple);
        System.out.println(appleBox2.getWeight());

        System.out.println("Сравним");
        System.out.println(appleBox1.compare(appleBox2));

        System.out.println("первая коробка апельсинов");

        Orange[] orangeArr1 = {
                new Orange(),
                new Orange(),
                new Orange(),
                new Orange()
        };

        Box<Orange> orangeBox1 = new Box<>();
        System.out.println(orangeBox1.getWeight());
        orangeBox1.addSeveral(orangeArr1);
        System.out.println(orangeBox1.getWeight());

        System.out.println("Сравнение коробок апельсинов и яблок");
        System.out.println(appleBox1.compare(orangeBox1));
        System.out.println(appleBox2.compare(orangeBox1));

        System.out.println("Пересыпаем саму в себя");
        appleBox1.refill(appleBox1);
        System.out.println(appleBox1.getWeight());
        System.out.println("не пересыпаетися");

        System.out.println("Пересыпаем");
        System.out.println(appleBox1.getWeight());
        System.out.println(appleBox2.getWeight());
        appleBox1.refill(appleBox2);
        System.out.println("пересыпалось");
        System.out.println(appleBox1.getWeight());
        System.out.println(appleBox2.getWeight());

    }
}
