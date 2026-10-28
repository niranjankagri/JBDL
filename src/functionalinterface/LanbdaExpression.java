package functionalinterface;

class LambdaExpression {
    public static void main(String[] args) {
        /* Lambda Expression*/
        FunctionalInterface f = () -> {
            System.out.println("Inside Test Function");
        };
        f.test();

    }
}
