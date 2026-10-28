package jbdl.session02.singleton;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class SingletonTest {

    @Test
    @DisplayName("Every kind returns the same object every time")
    void sameInstance() {
        assertThat(EagerConfig.getInstance()).isSameAs(EagerConfig.getInstance());
        assertThat(LazyConfig.getInstance()).isSameAs(LazyConfig.getInstance());
        assertThat(HolderConfig.getInstance()).isSameAs(HolderConfig.getInstance());
        assertThat(AppRegistry.INSTANCE).isSameAs(AppRegistry.valueOf("INSTANCE"));
    }

    @Test
    @Timeout(10)
    @DisplayName("64 threads asking at the same moment get one LazyConfig, created once")
    void lazyIsThreadSafe() throws Exception {
        int threads = 64;
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            List<Future<Object>> results = new ArrayList<>();
            Callable<Object> task = () -> {
                start.await(); // all threads wait here, then race
                return LazyConfig.getInstance();
            };
            for (int i = 0; i < threads; i++) {
                results.add(pool.submit(task));
            }
            start.countDown();
            Set<Object> distinct = ConcurrentHashMap.newKeySet();
            for (Future<Object> result : results) {
                distinct.add(result.get());
            }
            assertThat(distinct).hasSize(1);
            assertThat(LazyConfig.timesCreated()).isEqualTo(1);
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    @DisplayName("Reflection can break a class-based singleton by calling the private constructor")
    void reflectionBreaksClassSingleton() throws Exception {
        Constructor<EagerConfig> constructor = EagerConfig.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThat(constructor.newInstance()).isNotSameAs(EagerConfig.getInstance());
    }

    @Test
    @DisplayName("...but not an enum singleton: the JVM refuses to create enum objects by reflection")
    void enumIsSafeFromReflection() throws Exception {
        Constructor<AppRegistry> constructor = AppRegistry.class.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);

        assertThatThrownBy(() -> constructor.newInstance("SECOND", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("enum");
    }

    @Test
    @DisplayName("Enum singleton holds shared state for the whole application")
    void enumHoldsSharedState() {
        AppRegistry.INSTANCE.put("currency", "INR");

        assertThat(AppRegistry.valueOf("INSTANCE").get("currency")).isEqualTo("INR");
    }
}
