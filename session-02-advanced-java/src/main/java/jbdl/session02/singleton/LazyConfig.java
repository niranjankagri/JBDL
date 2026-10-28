package jbdl.session02.singleton;

/**
 * Lazy with double-checked locking: created on first use, locks only while creating. `volatile` is
 * required, or another thread could see a reference to a half-constructed object.
 */
public final class LazyConfig {

    private static volatile LazyConfig instance;
    private static int created; // for the tests: how many times the constructor ran

    private LazyConfig() {
        created++;
    }

    public static LazyConfig getInstance() {
        LazyConfig result = instance;
        if (result == null) {                      // 1st check, no lock: fast path once created
            synchronized (LazyConfig.class) {
                result = instance;
                if (result == null) {              // 2nd check, inside the lock: only one thread creates
                    result = new LazyConfig();
                    instance = result;
                }
            }
        }
        return result;
    }

    static synchronized int timesCreated() {
        return created;
    }
}
