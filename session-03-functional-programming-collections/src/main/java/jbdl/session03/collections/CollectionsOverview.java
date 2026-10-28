package jbdl.session03.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * The parts of the framework every collection shares: iteration (and its trap), natural ordering
 * (Comparable) vs custom ordering (Comparator), immutable collections and the Collections helpers.
 */
public final class CollectionsOverview {

    private CollectionsOverview() {
    }

    /** Comparable: the class defines ONE natural order (here by roll number). */
    public record Student(int rollNumber, String name, double marks) implements Comparable<Student> {
        @Override
        public int compareTo(Student other) {
            return Integer.compare(rollNumber, other.rollNumber);
        }
    }

    /**
     * Comparator: any number of orders defined outside the class, combinable. Read the chain left to
     * right: compare by marks, reverse it (highest first), and if marks are equal compare by name.
     * Written out as a class it would be:
     *   int compare(Student a, Student b) {
     *       int byMarks = Double.compare(b.marks(), a.marks());   // b before a = highest first
     *       if (byMarks != 0) { return byMarks; }
     *       return a.name().compareTo(b.name());
     *   }
     */
    public static final Comparator<Student> BY_MARKS_HIGH_FIRST_THEN_NAME =
            Comparator.comparingDouble(Student::marks).reversed().thenComparing(Student::name);

    public static List<Student> naturalOrder(List<Student> students) {
        List<Student> sorted = new ArrayList<>(students);
        Collections.sort(sorted); // uses compareTo
        return sorted;
    }

    public static List<Student> rankList(List<Student> students) {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort(BY_MARKS_HIGH_FIRST_THEN_NAME);
        return sorted;
    }

    /**
     * Removing inside a for-each loop throws ConcurrentModificationException: the list's iterator
     * notices the list changed behind its back ("fail-fast").
     */
    public static void removeFailingInForEach(List<Student> students) {
        for (Student student : students) {
            if (student.marks() < 40) {
                students.remove(student);
            }
        }
    }

    /** The safe ways: Iterator.remove() or (simpler) removeIf. */
    public static void removeFailingWithIterator(List<Student> students) {
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().marks() < 40) {
                iterator.remove();
            }
        }
    }

    public static void removeFailingWithRemoveIf(List<Student> students) {
        students.removeIf(student -> student.marks() < 40); // "remove every student whose marks are below 40"
    }
}
