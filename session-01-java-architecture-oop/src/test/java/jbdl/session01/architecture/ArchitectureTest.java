package jbdl.session01.architecture;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Topic 1 (Java architecture): checks what the running JVM reports, which class loader loads which
 * class (bootstrap / platform / app), and that Java passes arguments by value.
 */
class ArchitectureTest {

    @Test
    @DisplayName("The running JVM reports its version, processors and heap limit")
    void jvmInfo() {
        JvmInfo jvm = JvmInfo.current();

        assertThat(Runtime.version().feature()).isGreaterThanOrEqualTo(17);
        assertThat(jvm.javaVersion()).startsWith(String.valueOf(Runtime.version().feature()));
        assertThat(jvm.processors()).isPositive();
        assertThat(jvm.maxHeapMegabytes()).isPositive();
    }

    @Test
    @DisplayName("Core classes come from the bootstrap loader")
    void coreClassesUseBootstrap() {
        assertThat(String.class.getClassLoader()).isNull();
        assertThat(ClassLoaders.chainOf(String.class)).containsExactly(ClassLoaders.BOOTSTRAP);
    }

    @Test
    @DisplayName("Non-core JDK modules come from the platform loader")
    void jdkModulesUsePlatform() {
        assertThat(ClassLoaders.chainOf(java.sql.Date.class)).containsExactly("platform", ClassLoaders.BOOTSTRAP);
    }

    @Test
    @DisplayName("Our own classes come from the application loader, which delegates up the chain")
    void ownClassesUseApp() {
        // our class: loaded by the application loader, whose parents are platform and bootstrap
        assertThat(ClassLoaders.chainOf(ClassLoaders.class)).containsExactly("app", "platform", ClassLoaders.BOOTSTRAP);
    }

    @Test
    @DisplayName("A primitive argument is copied: the caller's variable does not change")
    void primitivesAreCopied() {
        int number = 5;

        StackAndHeap.addTen(number);

        assertThat(number).isEqualTo(5);
    }

    @Test
    @DisplayName("An object argument shares the object: changes to it are visible to the caller")
    void objectChangesAreShared() {
        StackAndHeap.Box box = new StackAndHeap.Box(5);

        StackAndHeap.addTen(box);

        assertThat(box.getValue()).isEqualTo(15);
    }

    @Test
    @DisplayName("Reassigning the parameter does not affect the caller's reference")
    void reassigningIsLocal() {
        StackAndHeap.Box box = new StackAndHeap.Box(5);

        StackAndHeap.replace(box);

        assertThat(box.getValue()).isEqualTo(5);
    }
}
