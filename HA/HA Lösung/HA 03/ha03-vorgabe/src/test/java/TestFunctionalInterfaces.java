import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static impl.FunctionalInterfaces.*;

public class TestFunctionalInterfaces {

    @Test
    @DisplayName("3.1.1 (2 Punkte) : testApplyMathToOneInt")
    public void testApplyMathToOneInt() {
        int modifiedNumber = -1;
        // modifiedNumber = applyMathToOneInt(15, i -> i + 3); //TODO: Zeile einkommentieren
        assertEquals(18, modifiedNumber);
    }

    @Test
    @DisplayName("3.1.2 (2 Punkte) : testApplyMathToTwoInts")
    public void testApplyMathToTwoInts() {
        int resultOfMath = -1;
        // resultOfMath = applyMathToTwoInts(10, 2, (int a, int b) -> (a % 3) + (b * 5)); //TODO: Zeile einkommentieren
        assertEquals(11, resultOfMath);
    }

    @Test
    @DisplayName("3.1.3 (2 Punkte) : testConcatenateInts")
    public void testConcatenateInts() {
        String concatenatedInts = "error";
        // concatenatedInts = concatenateInts(5, 3, (a, b) -> "One: " + a + ", Two: " + b); //TODO: Zeile einkommentieren
        assertEquals("One: 5, Two: 3", concatenatedInts);
    }

    @Test
    @DisplayName("3.1.4 (2 Punkte) : testFilterIntegers")
    public void testFilterIntegers() {
        List<Integer> nums = Arrays.asList(2, 3, 10, 4, 1, 6, 7, 8, 5, 9);
        Integer[] expected = {1, 2, 3, 4, 5};
        List<Integer> actual = new ArrayList<>();
        // actual = filterIntegers(nums, i -> i <= 5); //TODO: Zeile einkommentieren

        assertArrayEquals(expected, actual.stream().sorted().toArray());
    }
}
