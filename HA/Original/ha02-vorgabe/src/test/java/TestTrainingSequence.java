import impl.Gym;
import org.junit.jupiter.api.*;
import util.AbstractAthlete;
import util.GymMetrics;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestTrainingSequence {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void divertSystemOut() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreSystemOut() {
        System.setOut(originalOut);
    }

    @AfterEach
    void resetGymMetrics() {
        GymMetrics.reset();
    }

    @Test
    void testSequenceSingleAthleteSingleCycle() {
        final int numAthletes = 1;
        final int cycles = 1;

        setupAndRun(numAthletes, cycles);
        LinkedList<GymMetrics.AthleteAction> actions = GymMetrics.getActions();

        if (actions.size() != 6) {
            fail("Incorrect number of actions performed by athlete (expected: " + (cycles * 6) + ", actual: " + actions.size() + ").");
        }

        testSequenceStrict(actions, 0);
    }

    @Test
    void testSequenceSingleAthleteMultiCycle() {
        final int numAthletes = 1;
        final int cycles = 4;

        setupAndRun(numAthletes, cycles);
        LinkedList<GymMetrics.AthleteAction> actions = GymMetrics.getActions();

        if (actions.size() != cycles * 6) {
            fail("Incorrect number of actions performed by athlete (expected: " + (cycles * 6) + ", actual: " + actions.size() + ").");
        }

        for (int i = 0; i < cycles; i++) {
            testSequenceStrict(actions, i * 6);
        }
    }

    @Test
    void testSequenceMultiAttemptAllowFailures() {
        int cycles = 4;
        int numAthletes = 4;
        AbstractAthlete[] athletes = setupAndRun(numAthletes, cycles);

        LinkedList<GymMetrics.AthleteAction> actions = GymMetrics.getActions();

        for (AbstractAthlete a : athletes) {
            List<GymMetrics.AthleteAction> athleteActions = actions.stream()
                    .filter(action -> action.athleteId == a.getAthleteId())
                    .filter(action -> action.actionType != GymMetrics.ActionType.PICK_UP_FAILURE && action.actionType != GymMetrics.ActionType.PUT_DOWN_FAILURE)
                    .toList();

            testSequenceLenient(athleteActions);
        }
    }

    @Test
    void testSequenceMultiAttemptDisallowFailures() {
        final int numAthletes = 4;
        final int cycles = 4;

        AbstractAthlete[] athletes = setupAndRun(numAthletes, cycles);
        LinkedList<GymMetrics.AthleteAction> actions = GymMetrics.getActions();

        for (AbstractAthlete a : athletes) {
            List<GymMetrics.AthleteAction> athleteActions = actions.stream()
                    .filter(action -> action.athleteId == a.getAthleteId())
                    .toList();

            testSequenceLenient(athleteActions);
        }
    }

    @Test
    void testSequenceSingleAttemptAllowFailures() {
        final int numAthletes = 4;
        final int cycles = 4;

        AbstractAthlete[] athletes = setupAndRun(numAthletes, cycles);
        LinkedList<GymMetrics.AthleteAction> actions = GymMetrics.getActions();

        for (AbstractAthlete a : athletes) {
            List<GymMetrics.AthleteAction> athleteActions = actions.stream()
                    .filter(action -> action.athleteId == a.getAthleteId())
                    .filter(action -> action.actionType != GymMetrics.ActionType.PICK_UP_FAILURE && action.actionType != GymMetrics.ActionType.PUT_DOWN_FAILURE)
                    .toList();

            if (athleteActions.size() != cycles * 6) {
                fail("Incorrect number of actions performed by athletes (expected: " + (cycles * 6) + ", actual: " + athleteActions.size() + ").");
            }

            for (int i = 0; i < cycles; i++) {
                testSequenceStrict(athleteActions, i * 6);
            }
        }
    }

    @Test
    void testSequenceSingleAttemptsDisallowFailures() {
        final int numAthletes = 4;
        final int cycles = 4;

        AbstractAthlete[] athletes = setupAndRun(numAthletes, cycles);
        LinkedList<GymMetrics.AthleteAction> actions = GymMetrics.getActions();

        for (AbstractAthlete a : athletes) {
            List<GymMetrics.AthleteAction> athleteActions = actions.stream()
                    .filter(action -> action.athleteId == a.getAthleteId())
                    .toList();

            if (athleteActions.size() != cycles * 6) {
                fail("Incorrect number of actions performed by athletes (expected: " + (cycles * 6) + ", actual: " + athleteActions.size() + ").");
            }

            for (int i = 0; i < cycles; i++) {
                testSequenceStrict(athleteActions, i * 6);
            }
        }
    }

    private AbstractAthlete[] setupAndRun(int numAthletes, int cycles) {
        AbstractAthlete[] athletes = Gym.setup(numAthletes, cycles);
        numAthletes = Math.max(1, numAthletes);

        assertNotNull(athletes);

        if (athletes.length != numAthletes) {
            fail("Incorrect number of athletes created (expected: 1, actual: " + athletes.length + ").");
        }

        for (AbstractAthlete a : athletes) {
            a.start();
        }
        for (AbstractAthlete a : athletes) {
            try {
                a.join(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return athletes;
    }

    private void testSequenceStrict(List<GymMetrics.AthleteAction> athleteActions, int offset) {
        assertEquals(athleteActions.get(offset).actionType, GymMetrics.ActionType.STRETCH);
        assertEquals(athleteActions.get(offset + 1).actionType, GymMetrics.ActionType.PICK_UP_SUCCESS);
        assertEquals(athleteActions.get(offset + 2).actionType, GymMetrics.ActionType.PICK_UP_SUCCESS);
        assertEquals(athleteActions.get(offset + 3).actionType, GymMetrics.ActionType.EXERCISE);
        assertEquals(athleteActions.get(offset + 4).actionType, GymMetrics.ActionType.PUT_DOWN_SUCCESS);
        assertEquals(athleteActions.get(offset + 5).actionType, GymMetrics.ActionType.PUT_DOWN_SUCCESS);

        assertNotEquals(athleteActions.get(offset + 1).weightId, athleteActions.get(offset + 2).weightId);
        assertNotEquals(athleteActions.get(offset + 4).weightId, athleteActions.get(offset + 5).weightId);
    }

    private void testSequenceLenient(List<GymMetrics.AthleteAction> athleteActions) {
        // 0 = ready
        // 1 = stretching
        // 2 = picking up
        // 3 = done picking up
        // 4 = exercise
        // 5 = putting down
        // 6 = done putting down
        int currentPhase = 0;
        int nextPhase = 0;

        List<Integer> heldWeights = new LinkedList<>();

        for (GymMetrics.AthleteAction currentAction : athleteActions) {
            switch (currentAction.actionType) {
                case STRETCH -> nextPhase = 1;
                case EXERCISE -> nextPhase = 4;
                case PICK_UP_FAILURE -> fail("Athlete " + currentAction.athleteId + " failed to pick up a weight.");
                case PUT_DOWN_FAILURE -> fail("Athlete " + currentAction.athleteId + " failed to put down a weight.");
                case PICK_UP_SUCCESS -> {
                    heldWeights.add(currentAction.weightId);
                    if (heldWeights.size() == 2) {
                        assertNotEquals(heldWeights.get(0), heldWeights.get(1));
                        nextPhase = 3;
                    } else {
                        nextPhase = 2;
                    }
                }
                case PUT_DOWN_SUCCESS -> {
                    assertTrue(heldWeights.contains(currentAction.weightId));
                    heldWeights.remove(Integer.valueOf(currentAction.weightId));
                    if (currentPhase != 2) {
                        if (heldWeights.size() == 0) {
                            nextPhase = 6;
                        } else {
                            nextPhase = 5;
                        }
                    }
                }
            }

            if (heldWeights.size() > 2) {
                fail("Athlete " + currentAction.athleteId + " is holding an invalid number of weights (" + heldWeights.size() + ")");
            }

            if (nextPhase != currentPhase && nextPhase != currentPhase + 1) {
                fail("Athlete " + currentAction.athleteId + " performed an invalid action (" + currentAction.actionType + " transitioned from phase " + currentPhase + " to " + nextPhase + ")");
            }

            currentPhase = nextPhase < 6 ? nextPhase : 1;
        }

        if(nextPhase != 6) {
            fail("Athlete " + athleteActions.get(0).athleteId + " did not complete their sequence.");
        }
    }
}
