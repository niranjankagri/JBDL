package jbdl.session03.functional;

/**
 * The 2024 course task: one interface, two behaviours supplied as lambdas. (MetroEngine and
 * MetroEngine2 were identical, so they are one interface now.)
 */
@FunctionalInterface
public interface MetroEngine {

    String start();

    MetroEngine DRIVER_BASED = () -> "Drive with driver";
    MetroEngine DRIVERLESS = () -> "Drive without driver";
}
