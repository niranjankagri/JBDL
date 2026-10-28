package jbdl.session01.architecture;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JavaQuizTest {

    @Test
    @DisplayName("(byte) (127 + 1) wraps around to -128")
    void byteOverflowWraps() {
        assertThat(JavaQuiz.addOneAsByte((byte) 127)).isEqualTo((byte) -128);
        assertThat(JavaQuiz.addOneAsByte((byte) 5)).isEqualTo((byte) 6);
    }

    @Test
    @DisplayName("== on Integers: true inside the -128..127 cache, false outside")
    void integerCache() {
        assertThat(JavaQuiz.sameReference(1, 1)).isTrue();
        assertThat(JavaQuiz.sameReference(127, 127)).isTrue();
        assertThat(JavaQuiz.sameReference(1000, 1000)).isFalse();
    }

    @Test
    @DisplayName("a++ > 3 || a == 4 with a = 3 prints GFG")
    void postIncrement() {
        assertThat(JavaQuiz.postIncrementQuiz(3)).isEqualTo("GFG");
        assertThat(JavaQuiz.postIncrementQuiz(4)).isEqualTo("GFG"); // 4 > 3 short-circuits
        assertThat(JavaQuiz.postIncrementQuiz(1)).isEqualTo("Hi");
    }
}
