package ru.gb.lesson6;

public class NumbersMethods {

    public NumbersMethods() {
    }

//    1. Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
//    Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов, идущих
//    после последней четверки. Входной массив должен содержать хотя бы одну четверку, иначе в методе необходимо
//    выбросить RuntimeException.
    public static int[] methodOne(int [] numbers) throws RuntimeException{
        int pos = numbers.length;
        for (int i = numbers.length - 1; i >= 0; i--) {
            if (numbers[i] == 4) {
                pos = i;
                break;
            }
        }
        if (pos == numbers.length) {
            throw new RuntimeException("Четверки в массиве нет!");
        }
        int[] lastNumbers = new int[numbers.length - pos - 1];
        System.arraycopy(numbers, pos + 1, lastNumbers,0, numbers.length - pos - 1);
        return lastNumbers;
    }

//    2. Написать метод, который проверяет состав массива из чисел 1 и 4. Если в нем нет хоть одной четверки или
//    единицы, то метод вернет false; если в нем есть что то, кроме 1 и 4, то метод вернет false;
//    Написать набор тестов для этого метода (по 3-4 варианта входных данных).
//
    public static boolean methodTwo(int [] numbers) {
        int countFours = 0;
        int countOnes = 0;
        for (int i = 0; i < numbers.length; i++) {
            int number = numbers[i];
            System.out.println(number);
            if (number == 4) {
                countFours++;
            } else if (number == 1){
                countOnes++;
            }
            else {
                return false;

            }
        }
        return (countFours > 0 && countOnes > 0);
    }

}
