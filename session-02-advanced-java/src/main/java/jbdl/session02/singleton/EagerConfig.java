package jbdl.session02.singleton;

/** Eager: created when the class loads. Thread-safe for free (class loading is), even if never used. */
public final class EagerConfig {

    private static final EagerConfig INSTANCE = new EagerConfig();

    private EagerConfig() {
    }

    public static EagerConfig getInstance() {
        return INSTANCE;
    }
}
