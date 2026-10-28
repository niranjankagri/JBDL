package jbdl.session02.abstraction;

/**
 * Sealed interface (Java 17): only the listed classes may implement it. The compiler then knows
 * every case, so a switch over it needs no default branch and breaks if a new case is added.
 */
public sealed interface PaymentMethod permits PaymentMethod.Card, PaymentMethod.Upi, PaymentMethod.Cash {

    record Card(String lastFourDigits) implements PaymentMethod {
    }

    record Upi(String vpa) implements PaymentMethod {
    }

    record Cash() implements PaymentMethod {
    }

    static String describe(PaymentMethod method) {
        if (method instanceof Card card) {
            return "card ending " + card.lastFourDigits();
        } else if (method instanceof Upi upi) {
            return "UPI " + upi.vpa();
        }
        return "cash";
    }
}
