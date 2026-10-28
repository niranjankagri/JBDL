package multithreading;

public class ThreadJoin {

    public static void main(String[] args) {
        Thread t1 = new Thread( new ThreadExample() ); 
        Thread t2 = new Thread( new ThreadExample() );
        Thread t3 = new Thread( new ThreadExample() );
        t1.start();
        try {
            System.out.println("Current thread: " + Thread.currentThread().getName());
            t1.join();
        } catch (Exception e){
            System.out.println("Exception occured! " + e);
        }
        t2.start();
        try {
            System.out.println("Current thread: " + Thread.currentThread().getName());
            t2.join();
        } catch (Exception e){
            System.out.println("Exception occured! " + e);
        }
        t3.start();
        try {
            System.out.println("Current thread: " + Thread.currentThread().getName());
            t3.join();
        } catch (Exception e){
            System.out.println("Exception occured! " + e);
        }
    }

}

class ThreadExample implements Runnable {
    @Override
    public void run() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(5000);
                    System.out.println("Current thread: " + Thread.currentThread().getName());
                } catch (Exception e) {
                    System.out.println("Exception occured! " + e);
                }
                System.out.println(i);
            }
        });
    }
}
