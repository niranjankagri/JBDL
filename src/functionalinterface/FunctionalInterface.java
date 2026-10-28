package functionalinterface;

public interface FunctionalInterface {

    void test();

    default void name() {
        System.out.println("Hello");
    }

}

class Test implements FunctionalInterface {

    @Override
    public void test() {
        System.out.println("Inside Test Function");
    }

    public void classFunc() {
        System.out.println("Inside ClassFunc Function");
    }
}

class Main {
    public static void main(String[] args) {
        FunctionalInterface f = new Test();
        f.test();
        f.name();
//        f.classFunc(); // Not able to access classFunc using interface

        Test t = new Test();
        t.test();
        t.name();
        t.classFunc();
    }
}
