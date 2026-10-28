package multithreading;


public class RunnableInterface {
    public static void main(String[] args) {
        MyThread myThread1 = new MyThread();
        MyThread myThread2 = new MyThread();
        myThread1.start();
        myThread2.start();
    }
}

class MeThread implements Runnable {

    @Override
    public void run() {
        long currentThreadId = Thread.currentThread().getId();
        for (int i = 0; i < 5; i++) {
            System.out.println(i + " " + currentThreadId);
        }
    }

}
