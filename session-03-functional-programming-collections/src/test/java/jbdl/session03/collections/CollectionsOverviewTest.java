package jbdl.session03.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import jbdl.session03.collections.CollectionsOverview.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CollectionsOverviewTest {

    private static final List<Student> CLASS_LIST = List.of(
            new Student(3, "Ravi", 72.5),
            new Student(1, "Asha", 91.0),
            new Student(4, "Meena", 35.0),
            new Student(2, "Kiran", 91.0));

    @Test
    @DisplayName("Comparable gives the natural order (roll number)")
    void naturalOrder() {
        assertThat(CollectionsOverview.naturalOrder(CLASS_LIST)).extracting(Student::rollNumber)
                .containsExactly(1, 2, 3, 4);
    }

    @Test
    @DisplayName("A Comparator chain gives another order: marks high first, then name")
    void comparatorOrder() {
        assertThat(CollectionsOverview.rankList(CLASS_LIST)).extracting(Student::name)
                .containsExactly("Asha", "Kiran", "Ravi", "Meena");
    }

    @Test
    @DisplayName("Removing inside for-each fails fast with ConcurrentModificationException")
    void forEachRemoveFails() {
        List<Student> students = new ArrayList<>(List.of(
                new Student(4, "Meena", 35.0), new Student(1, "Asha", 91.0), new Student(2, "Kiran", 91.0)));

        assertThatThrownBy(() -> CollectionsOverview.removeFailingInForEach(students))
                .isInstanceOf(ConcurrentModificationException.class);
    }

    @Test
    @DisplayName("...except for the second-to-last element: no exception, the last one is silently skipped")
    void secondToLastQuirk() {
        List<Student> students = new ArrayList<>(List.of(
                new Student(1, "Asha", 91.0), new Student(4, "Meena", 35.0), new Student(5, "Vik", 20.0)));

        CollectionsOverview.removeFailingInForEach(students); // removes Meena, then hasNext() is false

        assertThat(students).extracting(Student::name).containsExactly("Asha", "Vik"); // Vik (20) never checked
    }

    @Test
    @DisplayName("Iterator.remove and removeIf remove safely")
    void safeRemoval() {
        List<Student> viaIterator = new ArrayList<>(CLASS_LIST);
        List<Student> viaRemoveIf = new ArrayList<>(CLASS_LIST);

        CollectionsOverview.removeFailingWithIterator(viaIterator);
        CollectionsOverview.removeFailingWithRemoveIf(viaRemoveIf);

        assertThat(viaIterator).hasSize(3).noneMatch(s -> s.name().equals("Meena"));
        assertThat(viaRemoveIf).isEqualTo(viaIterator);
    }

    @Test
    @DisplayName("List.of is immutable: any change throws")
    void immutableList() {
        assertThatThrownBy(() -> CLASS_LIST.add(new Student(5, "New", 50)))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
