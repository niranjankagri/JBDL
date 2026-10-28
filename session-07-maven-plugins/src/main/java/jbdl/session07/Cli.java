package jbdl.session07;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

/**
 * A tiny command-line tool, packaged two ways by the plugins in pom.xml:
 * - session-07-maven-plugins-1.0.0-SNAPSHOT.jar: our classes + Main-Class (needs commons-lang3 on the classpath)
 * - session-07-maven-plugins-1.0.0-SNAPSHOT-all.jar: shaded, commons-lang3 inside, runs with `java -jar` alone
 */
public final class Cli {

    private Cli() {
    }

    /** Turns words into a slug for a URL: "Maven Plugins 101!" -> "maven-plugins-101". */
    public static String slug(String text) {
        String cleaned = StringUtils.stripAccents(text).toLowerCase().replaceAll("[^a-z0-9]+", " ");
        return String.join("-", StringUtils.split(cleaned));
    }

    public static String run(String[] args) {
        if (args.length == 0) {
            return "usage: java -jar session-07-maven-plugins-1.0.0-SNAPSHOT-all.jar <words...>";
        }
        return slug(String.join(" ", Arrays.asList(args)));
    }

    public static void main(String[] args) {
        System.out.println(run(args));
    }
}
