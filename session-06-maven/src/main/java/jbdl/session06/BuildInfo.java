package jbdl.session06;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Reads build-info.properties from the classpath. Maven filled in its ${...} placeholders while
 * copying it to target/classes (process-resources phase), so this is the POM's data at run time.
 */
public record BuildInfo(String group, String artifact, String version, String javaRelease, String commonsLang3) {

    public static BuildInfo load() {
        Properties properties = new Properties();
        // a leading "/" means the root of the classpath, i.e. target/classes or the root of the jar
        try (InputStream in = BuildInfo.class.getResourceAsStream("/build-info.properties")) {
            if (in == null) {
                throw new IllegalStateException("build-info.properties is not on the classpath");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return new BuildInfo(
                properties.getProperty("group"),
                properties.getProperty("artifact"),
                properties.getProperty("version"),
                properties.getProperty("java.release"),
                properties.getProperty("commons-lang3"));
    }

    /** Maven coordinates in the usual groupId:artifactId:version form. */
    public String coordinates() {
        return group + ":" + artifact + ":" + version;
    }

    public static void main(String[] args) {
        BuildInfo info = load();
        System.out.println("Built from:     " + info.coordinates());
        System.out.println("Java release:   " + info.javaRelease());
        System.out.println("commons-lang3:  " + info.commonsLang3());
        System.out.println(TextTools.headline("maven builds, tests and packages this module", 30));
    }
}
