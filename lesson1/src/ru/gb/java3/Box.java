package ru.gb.java3;

import ru.gb.java3.fruits.Fruit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Box<T extends Fruit> {

    private List<T> container;

    public Box() {
        this.container = new ArrayList<>();
    }

    public Box(T... fruitsArr) {
        this.container = new ArrayList<>(Arrays.asList(fruitsArr));
    }

    public void add(T fruit) {
        container.add(fruit);
    }

    //Добавил для удобства тестирования
    public void addSeveral(T... fruitsArr) {
        container.addAll(Arrays.asList(fruitsArr));
    }

    public double getWeight() {
        double boxWeight = 0;
        for (T t : container) {
            boxWeight += t.getWeight();
        }
        return boxWeight;
    }

    public boolean compare(Box<?> box) {
        return Math.abs(this.getWeight() - box.getWeight()) < 0.0001;
    }

    public void refill(Box<? super T> anotherBox) {
        //нельзя пересыпать в саму себя
        if (anotherBox == this) {
            return;
        }
        anotherBox.container.addAll(this.container);
        this.container.clear();
    }

}
