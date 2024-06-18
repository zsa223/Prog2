import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.AbstractAthlete;
import util.AbstractWeight;
import impl.Gym;
import util.GymMetrics;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class TestGymSetup {

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
    void testAthleteArray() {
        AbstractAthlete[] returnedAthletes = Gym.setup(20, 20);
        LinkedList<AbstractAthlete> athletes = GymMetrics.getAthletes();

        assertArrayEquals(athletes.toArray(), returnedAthletes);
        assertEquals(athletes.size(), 20);
        for(AbstractAthlete a : GymMetrics.getAthletes()) {
            assertNotNull(a);
        }
    }

    @Test
    void testWeightArray() {
        Gym.setup(20, 20);
        LinkedList<AbstractWeight> weights = GymMetrics.getWeights();

        assertEquals(weights.size(), 20);
        for(AbstractWeight w : weights) {
            assertNotNull(w);
        }
    }

    @Test
    void testArrayMinSizes() {
        Gym.setup(0, 1);
        LinkedList<AbstractAthlete> athletes = GymMetrics.getAthletes();
        LinkedList<AbstractWeight> weights = GymMetrics.getWeights();

        assertEquals(athletes.size(), 1);
        assertEquals(weights.size(), 2);
    }

    @Test
    void testAthleteWeights() {
        Gym.setup(20, 20);
        LinkedList<AbstractAthlete> athletes = GymMetrics.getAthletes();

        // make sure the list is populated
        assertEquals(athletes.size(), 20);
        for(AbstractAthlete w : athletes) {
            assertNotNull(w);
        }

        for(int i = 0; i < athletes.size(); i++) {
            AbstractWeight w1 = athletes.get(i).getRightWeight();
            AbstractWeight w2 = athletes.get((i + 1) % athletes.size()).getLeftWeight();
            assertEquals(w1, w2);
        }
    }

    @Test
    void testAthleteCycles() {
        Gym.setup(20, 20);
        LinkedList<AbstractAthlete> athletes = GymMetrics.getAthletes();

        // make sure the list is populated
        assertEquals(athletes.size(), 20);
        for(AbstractAthlete w : athletes) {
            assertNotNull(w);
        }

        for(AbstractAthlete a : athletes) {
            assertEquals(20, a.getCycles());
        }
    }
}
