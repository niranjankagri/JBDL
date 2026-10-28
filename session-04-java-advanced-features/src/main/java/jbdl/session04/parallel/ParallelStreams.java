package jbdl.session04.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Sequential vs parallel streams. A parallel stream splits the source, runs the parts on the
 * ForkJoinPool's common pool and combines the results. Same answer for stateless operations and
 * associative reductions; different behaviour for order and for shared mutable state.
 */
public final class ParallelStreams {

    private ParallelStreams() {
    }

    public static long sumOfSquares(int n, boolean parallel) {
        LongStream numbers = LongStream.rangeClosed(1, n);
        return (parallel ? numbers.parallel() : numbers).map(x -> x * x).sum();
    }

    /** Which threads did the work: just the caller sequentially; the caller + common-pool workers in parallel. */
    public static Set<String> threadsUsed(boolean parallel) {
        Set<String> threads = ConcurrentHashMap.newKeySet();
        IntStream numbers = IntStream.range(0, 10_000);
        (parallel ? numbers.parallel() : numbers).forEach(i -> threads.add(Thread.currentThread().getName()));
        return threads;
    }

    /** forEachOrdered keeps the source order even in parallel (at some cost); forEach does not. */
    public static List<Integer> orderedOutput(int n) {
        List<Integer> out = new ArrayList<>();
        IntStream.range(0, n).parallel().forEachOrdered(out::add); // forEachOrdered runs one at a time
        return out;
    }

    /** The right way to build a list from a parallel stream: let collect/toList combine the parts. */
    public static List<Integer> collectSafely(int n) {
        return IntStream.range(0, n).parallel().boxed().toList();
    }

    /**
     * The WRONG way: many threads add to one ArrayList at once. Elements get lost or the add throws
     * ArrayIndexOutOfBoundsException. Shown in the README; only for demonstration.
     */
    public static int unsafeAddCount(int n) {
        List<Integer> shared = new ArrayList<>();
        try {
            IntStream.range(0, n).parallel().forEach(shared::add);
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
        return shared.size();
    }

    public static void main(String[] args) {
        int n = 2_000_000; // the sum of squares up to n must fit in a long (n = 5 million would overflow)
        for (boolean parallel : new boolean[] {false, true}) {
            long start = System.nanoTime();
            long sum = sumOfSquares(n, parallel);
            long millis = (System.nanoTime() - start) / 1_000_000;
            System.out.printf("%-10s sum=%d in %d ms, threads=%d%n",
                    parallel ? "parallel" : "sequential", sum, millis, threadsUsed(parallel).size());
        }
        System.out.println("Unsafe parallel add of 100000 items kept: " + unsafeAddCount(100_000) + " (-1 = crashed)");
        System.out.println("Common pool parallelism: " + java.util.concurrent.ForkJoinPool.commonPool().getParallelism());
    }
}
