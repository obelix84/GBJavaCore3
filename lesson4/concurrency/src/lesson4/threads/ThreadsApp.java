package lesson4.threads;

public class ThreadsApp {
    static final Object monitor = new Object();
    static volatile char curLetter = 'A';
    static final int count = 5;

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                for (int i = 0; i < count; i++) {
                    synchronized (monitor) {
                        while (curLetter != 'A') {
                            monitor.wait();
                        }
                        System.out.print("A");
                        curLetter = 'B';
                        monitor.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                for (int i = 0; i < count; i++) {
                    synchronized (monitor) {
                        while (curLetter != 'B') {
                            monitor.wait();
                        }
                        System.out.print("B");
                        curLetter = 'C';
                        monitor.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                for (int i = 0; i < count; i++) {
                    synchronized (monitor) {
                        while (curLetter != 'C') {
                            monitor.wait();
                        }
                        System.out.print("C");
                        curLetter = 'A';
                        monitor.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
