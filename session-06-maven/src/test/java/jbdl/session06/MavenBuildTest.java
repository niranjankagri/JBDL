package jbdl.session06;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests that only pass because Maven did its job: process-resources filtered the POM's values into
 * build-info.properties, and the compile-scope dependency was downloaded and put on the classpath.
 */
class MavenBuildTest {

    @Test
    @DisplayName("Resource filtering put the POM's values into build-info.properties")
    void filteredResource() {
        BuildInfo info = BuildInfo.load();

        assertThat(info.coordinates()).isEqualTo("io.github.niranjankagri:session-06-maven:1.0.0-SNAPSHOT");
        assertThat(info.javaRelease()).isEqualTo("17");          // inherited from the parent POM
        assertThat(info.commonsLang3()).isEqualTo("3.21.0");     // a property of this POM
        assertThat(info.version()).doesNotContain("${");          // nothing left unreplaced
    }

    @Test
    @DisplayName("The compile-scope dependency is on the classpath and works")
    void dependencyIsUsable() {
        assertThat(TextTools.headline("  maven   manages   dependencies  ", 15)).isEqualTo("Maven manage..."); // 15 characters including "..."
        assertThat(TextTools.isBlank(" \t")).isTrue();
        assertThat(TextTools.isBlank(null)).isTrue();
    }

    @Test
    @DisplayName("The library class was loaded from the commons-lang3 jar Maven downloaded")
    void loadedFromTheDownloadedJar() {
        String location = StringUtils.class.getProtectionDomain().getCodeSource().getLocation().toString();

        assertThat(location).contains("commons-lang3").endsWith(".jar");
    }
}
