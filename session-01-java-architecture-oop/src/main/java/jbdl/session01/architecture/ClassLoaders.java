package jbdl.session01.architecture;

import java.util.ArrayList;
import java.util.List;

/**
 * The class loader chain that loaded a class. Loaders delegate to their parent first, so core classes
 * like String always come from the bootstrap loader (built into the JVM, shown as null in Java).
 */
public final class ClassLoaders {

    public static final String BOOTSTRAP = "bootstrap";

    private ClassLoaders() {
    }

    /** From the loader that loaded the class up to the bootstrap loader, e.g. [app, platform, bootstrap]. */
    public static List<String> chainOf(Class<?> type) {
        List<String> chain = new ArrayList<>();
        for (ClassLoader loader = type.getClassLoader(); loader != null; loader = loader.getParent()) {
            chain.add(loader.getName());
        }
        chain.add(BOOTSTRAP);
        return chain;
    }

    public static void main(String[] args) {
        System.out.println("String (java.base):        " + chainOf(String.class));
        System.out.println("java.sql.Date (java.sql):  " + chainOf(java.sql.Date.class));
        System.out.println("ClassLoaders (this course): " + chainOf(ClassLoaders.class));
    }
}
