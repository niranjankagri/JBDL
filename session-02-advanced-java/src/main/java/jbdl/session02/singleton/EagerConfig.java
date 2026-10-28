package jbdl.session02.singleton;

/** Eager: created when the class loads. Thread-safe for free (class loading is), even if never used. */
public final class EagerConfig {

    private static final EagerConfig INSTANCE = new EagerConfig();

    /** private: no other class can call new EagerConfig(). */
    private EagerConfig() {
    }

    /** Always the same object, created when the class was loaded. */
    public static EagerConfig getInstance() {
        return INSTANCE;
    }
}
