package jbdl.session06;

import org.apache.commons.lang3.StringUtils;

/**
 * Uses a library Maven downloaded from Maven Central (commons-lang3). Without Maven you would
 * download the jar by hand, put it on the classpath for javac and java, and repeat that for every
 * jar it needs and every developer machine.
 */
public final class TextTools {

    private TextTools() {
    }

    /** Capitalised and shortened with "..." to at most maxWidth characters; null-safe. */
    public static String headline(String text, int maxWidth) {
        return StringUtils.abbreviate(StringUtils.capitalize(StringUtils.normalizeSpace(text)), maxWidth);
    }

    public static boolean isBlank(String text) {
        return StringUtils.isBlank(text);
    }
}
