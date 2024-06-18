package util;

import java.util.Objects;

public abstract class AbstractAthlete extends Thread {

    protected final int id;

    protected final int cycles;

    protected final AbstractWeight leftWeight;
    protected final AbstractWeight rightWeight;

    public AbstractAthlete(int id, int cycles, AbstractWeight leftWeight, AbstractWeight rightWeight) {
        this.id = id;
        this.cycles = cycles;
        this.leftWeight = leftWeight;
        this.rightWeight = rightWeight;
        GymMetrics.registerAthlete(this);
    }

    protected void stretch() {
        long time = (long) (Math.random() * 100);
        //System.out.println("Athlete " + id + " stretching for " + time + "ms.");
        GymMetrics.registerAthleteStretching(this);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("Athlete " + id + " done stretching.");
    }

    protected void exercise() {
        long time = (long) (Math.random() * 100);
        //System.out.println("Athlete " + id + " exercising for " + time + "ms.");
        GymMetrics.registerAthleteExercising(this);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("Athlete " + id + " done exercising.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAthlete that = (AbstractAthlete) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getAthleteId() {
        return id;
    }

    public int getCycles() {
        return cycles;
    }

    public AbstractWeight getLeftWeight() {
        return leftWeight;
    }

    public AbstractWeight getRightWeight() {
        return rightWeight;
    }
}
