import impl.Gym;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.AbstractAthlete;
import util.AbstractWeight;
import util.GymMetrics;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestWeightAccess {

    private static final int numAthletes = 4;
    private static final int cycles = 4;

    @BeforeAll
    static void setupAndRun() {

        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        AbstractAthlete[] athletes = Gym.setup(numAthletes, cycles);
        int correctedNumAthletes = Math.max(1, numAthletes);

        if(athletes == null || athletes.length != correctedNumAthletes) {
            return;
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

        System.setOut(System.out);
    }

    @AfterAll
    static void resetGymMetrics() {
        GymMetrics.reset();
    }

    @Test
    void testEveryWeightUsed() {
        assertEveryWeightUsed();
    }

    @Test
    void testEveryWeightUsedCorrectAmountLenient() {
        assertEveryWeightUsed();

        LinkedList<GymMetrics.AthleteAction> athleteActions = GymMetrics.getActions();

        // Bonusaufgabe (0P): nachvollziehen, was diese Kette von Stream-Operationen macht - gutes Training für die Klausur!
        assertTrue(
                athleteActions.stream()
                        .filter(a -> a.actionType == GymMetrics.ActionType.PICK_UP_SUCCESS || a.actionType == GymMetrics.ActionType.PUT_DOWN_SUCCESS)
                        .collect(Collectors.groupingBy(
                                GymMetrics.AthleteAction::getWeightId,
                                Collectors.mapping(
                                        GymMetrics.AthleteAction::getActionType,
                                        Collectors.toList())))
                        .values().stream()
                        .map(actions -> actions.stream()
                                .map(at -> at == GymMetrics.ActionType.PICK_UP_SUCCESS ? 1 : -1)
                                .reduce(Integer::sum).orElse(-1))
                        .map(v -> v == 0)
                        .reduce((acc, x) -> acc && x).orElse(false));
    }

    @Test
    void testEveryWeightUsedCorrectAmountStrict() {
        assertEveryWeightUsed();

        LinkedList<GymMetrics.AthleteAction> athleteActions = GymMetrics.getActions();
        LinkedList<AbstractWeight> weights = GymMetrics.getWeights();

        for(AbstractWeight weight : weights) {
            assertEquals(athleteActions.stream()
                    .filter(a -> a.weightId == weight.getWeightId())
                    .filter(a -> a.actionType == GymMetrics.ActionType.PICK_UP_SUCCESS)
                    .count(), cycles * 2);

            assertEquals(athleteActions.stream()
                    .filter(a -> a.weightId == weight.getWeightId())
                    .filter(a -> a.actionType == GymMetrics.ActionType.PUT_DOWN_SUCCESS)
                    .count(), cycles * 2);
        }
    }

    @Test
    void testExclusiveAccess() {
        assertEveryWeightUsed();
        LinkedList<GymMetrics.AthleteAction> athleteActions = GymMetrics.getActions();
        LinkedList<AbstractWeight> weights = GymMetrics.getWeights();

        for(AbstractWeight weight : weights) {
            List<GymMetrics.AthleteAction> actionsOfWeight = athleteActions.stream()
                    .filter(a -> a.weightId == weight.getWeightId())
                    .toList();

            for(int i = 0; i < actionsOfWeight.size() - 1; i++) {
                assertNotEquals(actionsOfWeight.get(i).actionType, actionsOfWeight.get(i + 1).actionType);
            }
        }
    }

    private void assertEveryWeightUsed() {
        LinkedList<GymMetrics.AthleteAction> actions = GymMetrics.getActions();
        LinkedList<AbstractWeight> weights = GymMetrics.getWeights();

        assertNotEquals(0, actions.size());
        assertNotEquals(0, weights.size());

        List<Integer> weightsInActions = actions.stream().filter(a -> a.actionType == GymMetrics.ActionType.PICK_UP_SUCCESS).map(a -> a.weightId).toList();
        assertTrue(weights.stream().map(w -> weightsInActions.contains(w.getWeightId())).reduce((acc, x) -> acc && x).orElse(false));
    }
}
