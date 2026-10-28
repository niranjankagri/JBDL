package jbdl.session03.functional;

/** The "old way" to implement an interface: a named class. */
public class FormalGreeting implements Greeting {

    /** The interface's one abstract method, implemented the long way. */
    @Override
    public String greet(String name) {
        return "Good morning, " + name;
    }

    /** Not part of Greeting: reachable through a FormalGreeting variable, not through a Greeting one. */
    public String signature() {
        return "Kind regards";
    }
}
