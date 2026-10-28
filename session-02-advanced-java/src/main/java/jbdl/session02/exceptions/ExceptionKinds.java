package jbdl.session02.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The hierarchy, decided in code:
 *
 *   Throwable
 *   ├── Error                 serious JVM problems (OutOfMemoryError, StackOverflowError): don't catch
 *   └── Exception             CHECKED: the compiler forces you to catch or declare it (IOException, SQLException)
 *       └── RuntimeException  UNCHECKED: programming errors (NullPointerException, IllegalArgumentException)
 */
public final class ExceptionKinds {

    public enum Kind { ERROR, CHECKED, UNCHECKED }

    private ExceptionKinds() {
    }

    public static Kind kindOf(Throwable throwable) {
        if (throwable instanceof Error) {
            return Kind.ERROR;
        }
        if (throwable instanceof RuntimeException) {
            return Kind.UNCHECKED;
        }
        return Kind.CHECKED;
    }

    /** catch order matters: the most specific type first, or the compiler rejects the later block. */
    public static String handle(IoAction action) {
        try {
            action.run();
            return "ok";
        } catch (FileNotFoundException e) {
            return "missing file: " + e.getMessage();
        } catch (IOException e) {
            return "other I/O problem: " + e.getMessage();
        } catch (IllegalArgumentException | IllegalStateException e) {
            // multi-catch (Java 7): one block for unrelated types
            return "bad input or state: " + e.getMessage();
        }
    }

    /** A piece of work that may throw a checked IOException. */
    @FunctionalInterface
    public interface IoAction {
        void run() throws IOException;
    }
}
