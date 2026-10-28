package jbdl.session07;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Enough tests to keep JaCoCo's 90 % line-coverage rule (pom.xml) green. */
class CliTest {

    @Test
    @DisplayName("Words become a URL slug")
    void slug() {
        assertThat(Cli.slug("Maven Plugins 101!")).isEqualTo("maven-plugins-101");
        assertThat(Cli.slug("  Café   Résumé ")).isEqualTo("cafe-resume");
    }

    @Test
    @DisplayName("Arguments are joined; no arguments prints the usage line")
    void run() {
        assertThat(Cli.run(new String[] {"Spring", "Boot"})).isEqualTo("spring-boot");
        assertThat(Cli.run(new String[0])).startsWith("usage:");
    }

    @Test
    @DisplayName("main prints the slug (this test lifted coverage from 71 % to the required 90 %)")
    void mainPrints() {
        PrintStream original = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
        try {
            Cli.main(new String[] {"Maven", "Rocks"});
        } finally {
            System.setOut(original);
        }
        assertThat(out.toString(StandardCharsets.UTF_8).strip()).isEqualTo("maven-rocks");
    }
}
