package ru.gb.lesson6;

import org.junit.jupiter.api.Assertions;

public class MainApp{

    public static void main(String[] args) {

        NumbersMethods numbersMethods = new NumbersMethods();
        int [] numbers = {1,2,4,4,2,3,4,1,7};
        int [] exp = {1,7};
       numbersMethods.methodOne(numbers);
    }


}
