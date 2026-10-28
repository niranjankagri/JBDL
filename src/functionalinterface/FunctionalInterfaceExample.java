package functionalinterface;

class FunctionalInterfaceExample {
    public static void main(String[] args) {
        FunctionalInterface t = new FunctionalInterface() {
            @Override
            public void test() {
                System.out.println("Inside Test Function");
            }
        };
        t.test();
    }
}
