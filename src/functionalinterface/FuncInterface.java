package functionalinterface;

public interface FuncInterface {

    String test();
}

class MainClass{
    public static void main(String[] args) {
        FuncInterface f = () -> "Return from Test Function";
        System.out.println(f.test());
    }
}
