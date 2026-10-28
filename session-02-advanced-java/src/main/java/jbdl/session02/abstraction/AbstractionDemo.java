package jbdl.session02.abstraction;

import java.util.List;

public final class AbstractionDemo {

    private AbstractionDemo() {
    }

    public static void main(String[] args) {
        List<Payable> payables = List.of(
                new Developer("Asha", 1_200_000),
                new Manager("Ravi", 2_400_000, 6),
                new Invoice("Cloud hosting", 15_000));
        payables.forEach(p -> System.out.println(p + " -> monthly " + p.monthlyPayInRupees()));
        System.out.println("Total monthly: " + Payable.totalMonthly(payables));
        System.out.println("Manager tax: " + new Manager("Ravi", 2_400_000, 6).taxInRupees());
        System.out.println("Smartphone: " + new SmartPhone().describe());
        System.out.println(PaymentMethod.describe(new PaymentMethod.Upi("niranjan@upi")));
    }
}
