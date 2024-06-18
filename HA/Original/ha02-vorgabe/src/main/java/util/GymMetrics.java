package util;

import java.util.LinkedList;

public class GymMetrics {

    public enum ActionType {
        STRETCH,
        EXERCISE,
        PICK_UP_SUCCESS,
        PICK_UP_FAILURE,
        PUT_DOWN_SUCCESS,
        PUT_DOWN_FAILURE
    }

    private static final LinkedList<AbstractAthlete> athletes = new LinkedList<>();

    private static final LinkedList<AbstractWeight> weights = new LinkedList<>();
    private static final LinkedList<AthleteAction> actions = new LinkedList<>();

    public static void registerAthlete(AbstractAthlete athlete) {
        synchronized (athletes) {
            athletes.add(athlete);
        }
    }

    public static void registerWeight(AbstractWeight weight) {
        synchronized (weights) {
            weights.add(weight);
        }
    }

    public static void registerWeightPickedUp(AbstractAthlete athlete, AbstractWeight weight, boolean success) {
        synchronized (actions) {
            actions.add(new AthleteAction(athlete.getAthleteId(), weight.getWeightId(), success ? ActionType.PICK_UP_SUCCESS : ActionType.PICK_UP_FAILURE));
        }
    }

    public static void registerWeightPutDown(AbstractAthlete athlete, AbstractWeight weight, boolean success) {
        synchronized (actions) {
            actions.add(new AthleteAction(athlete.getAthleteId(), weight.getWeightId(), success ? ActionType.PUT_DOWN_SUCCESS : ActionType.PUT_DOWN_FAILURE));
        }
    }

    public static void registerAthleteStretching(AbstractAthlete athlete) {
        synchronized (actions) {
            actions.add(new AthleteAction(athlete.getAthleteId(), -1, ActionType.STRETCH));
        }
    }

    public static void registerAthleteExercising(AbstractAthlete athlete) {
        synchronized (actions) {
            actions.add(new AthleteAction(athlete.getAthleteId(), -1, ActionType.EXERCISE));
        }
    }

    public static LinkedList<AbstractAthlete> getAthletes() {
        synchronized (athletes) {
            return athletes;
        }
    }

    public static LinkedList<AbstractWeight> getWeights() {
        synchronized (weights) {
            return weights;
        }
    }

    public static LinkedList<AthleteAction> getActions() {
        synchronized (actions) {
            return actions;
        }
    }

    public static void reset() {
        synchronized (athletes) {
            athletes.clear();
            synchronized (weights) {
                weights.clear();
                synchronized (actions) {
                    actions.clear();
                }
            }
        }
    }

    public static class AthleteAction {

        public final int athleteId;
        public final int weightId;
        public final GymMetrics.ActionType actionType;

        public AthleteAction(int athleteId, int weightId, GymMetrics.ActionType actionType) {
            this.athleteId = athleteId;
            this.weightId = weightId;
            this.actionType = actionType;
        }

        public int getAthleteId() {
            return athleteId;
        }

        public int getWeightId() {
            return weightId;
        }

        public ActionType getActionType() {
            return actionType;
        }
    }
}
