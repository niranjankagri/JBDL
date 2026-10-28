package multithreading;

public class MultiThreading {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            long currentThreadId = Thread.currentThread().getId();
            for (int i = 0; i < 50; i++) {
                System.out.println(i + " " + currentThreadId);
            }
        });
        Thread t2 = new Thread(() -> {
            long currentThreadId = Thread.currentThread().getId();
            for (int i = 0; i < 50; i++) {
                System.out.println(i + " " + currentThreadId);
            }
        });
        t1.start();
        t2.start();
    }

}
