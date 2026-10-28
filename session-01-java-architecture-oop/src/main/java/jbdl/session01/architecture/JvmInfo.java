package jbdl.session01.architecture;

/**
 * What the running JVM says about itself. The same .class files run on any of these: the JVM, not
 * the compiler, adapts them to the operating system and CPU ("write once, run anywhere").
 */
public record JvmInfo(String javaVersion, String vendor, String vmName, String operatingSystem,
                      int processors, long maxHeapMegabytes) {

    /** Reads the facts from system properties and the Runtime of the JVM running this code. */
    public static JvmInfo current() {
        Runtime runtime = Runtime.getRuntime();
        return new JvmInfo(
                System.getProperty("java.version"),
                System.getProperty("java.vendor"),
                System.getProperty("java.vm.name"),
                System.getProperty("os.name") + " " + System.getProperty("os.arch"),
                runtime.availableProcessors(),
                // -Xmx sets this limit; without it the JVM picks a share of the machine's memory
                runtime.maxMemory() / (1024 * 1024));
    }

    /** Prints the facts; run it on two machines or JDKs to see what changes and what doesn't. */
    public static void main(String[] args) {
        JvmInfo jvm = current();
        System.out.println("Java version:    " + jvm.javaVersion());
        System.out.println("Vendor:          " + jvm.vendor());
        System.out.println("JVM:             " + jvm.vmName());
        System.out.println("Operating system: " + jvm.operatingSystem());
        System.out.println("Processors:      " + jvm.processors());
        System.out.println("Max heap:        " + jvm.maxHeapMegabytes() + " MB");
    }
}
