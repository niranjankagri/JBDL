package jbdl.inheritance;

/**
 * A racing car: everything a Car has plus its racing record. getInfo() is overridden and reuses the
 * parent's text through super.getInfo() (the 2024 version returned a fixed placeholder string).
 */
public class SuperCar extends Car {

    private final int firstRaceYear;
    private final int trophiesWon;

    public SuperCar(String brand, String color, String name, int firstRaceYear, int trophiesWon) {
        super(brand, color, name);
        this.firstRaceYear = firstRaceYear;
        this.trophiesWon = trophiesWon;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + ", racing since " + firstRaceYear + " with " + trophiesWon + " trophies";
    }

    public int getFirstRaceYear() {
        return firstRaceYear;
    }

    public int getTrophiesWon() {
        return trophiesWon;
    }
}
