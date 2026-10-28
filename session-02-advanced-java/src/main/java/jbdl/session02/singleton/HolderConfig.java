package jbdl.session02.singleton;

/**
 * Initialization-on-demand holder: lazy AND thread-safe without locks. The nested class is loaded
 * (and INSTANCE created) only when getInstance() first touches it.
 */
public final class HolderConfig {

    private HolderConfig() {
    }

    private static final class Holder {
        private static final HolderConfig INSTANCE = new HolderConfig();
    }

    public static HolderConfig getInstance() {
        return Holder.INSTANCE;
    }
}
