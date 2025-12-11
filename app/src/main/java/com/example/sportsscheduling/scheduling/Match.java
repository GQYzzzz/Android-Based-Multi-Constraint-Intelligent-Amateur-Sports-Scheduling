package com.example.sportsscheduling.scheduling;

/**
 * The type Match.
 */
public class Match {
    private Team A;
    private Team B;
    private Venue v;
    private String time; // e.g. "09am", "01pm"
    private int day;

    /**
     * Instantiates a new Match.
     *
     * @param A    the a
     * @param B    the b
     * @param v    the v
     * @param time the time
     * @param day  the day
     */
    public Match(Team A, Team B, Venue v, String time, int day) {
        this.A = A;
        this.B = B;
        this.v = v;
        this.time = time;
        this.day = day;
    }

    /**
     * Gets a.
     *
     * @return the a
     */
    public Team getA() {
        return A;
    }

    /**
     * Sets a.
     *
     * @param a the a
     */
    public void setA(Team a) {
        A = a;
    }

    /**
     * Gets b.
     *
     * @return the b
     */
    public Team getB() {
        return B;
    }

    /**
     * Sets b.
     *
     * @param b the b
     */
    public void setB(Team b) {
        B = b;
    }

    /**
     * Gets v.
     *
     * @return the v
     */
    public Venue getV() {
        return v;
    }

    /**
     * Sets v.
     *
     * @param v the v
     */
    public void setV(Venue v) {
        this.v = v;
    }

    /**
     * Gets day.
     *
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets day.
     *
     * @param day the day
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return A.getName() + " vs " + B.getName() + " @ Day " + day + " " + v.getName() + " " + time;
    }
}
