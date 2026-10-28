package jbdl.testdata;

/** Shared inputs for the tests, so every test class uses the same values and they live in one place. */
public final class TestData {

    private TestData() {
    }

    /** Unsorted, with a duplicate (10), so ordering and de-duplication both show up. */
    public static final int[] NUMBERS = {30, 10, 20, 10};

    public static final String FULL_NAME = "Niranjan Kumar";

    public static final String BRAND = "Hyundai";
    public static final String MODEL = "Santro";
    public static final String COLOR = "Red";

    public static final String RACING_BRAND = "McLaren";
    public static final String RACING_MODEL = "F1";
    public static final int FIRST_RACE_YEAR = 2005;
    public static final int TROPHIES = 40;
}
