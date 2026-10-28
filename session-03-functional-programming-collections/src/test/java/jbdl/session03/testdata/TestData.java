package jbdl.session03.testdata;

/** Shared inputs for the Session 3 tests, so every test class uses the same values. */
public final class TestData {

    private TestData() {
    }

    /** Unsorted, with a duplicate (10), so ordering and de-duplication both show up. */
    public static final int[] NUMBERS = {30, 10, 20, 10};

    public static final String FULL_NAME = "Niranjan Kumar";
}
