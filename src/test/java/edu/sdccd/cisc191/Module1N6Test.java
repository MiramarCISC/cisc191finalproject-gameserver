package edu.sdccd.cisc191;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Module1N6Test {

    @Test
    void testHighScoreWithMultipleScores() {
        PlayerStats.scoreHistory.add(5);
        PlayerStats.scoreHistory.add(12);
        PlayerStats.scoreHistory.add(7);

        int highScore = PlayerStats.scoreHistory.stream()
                .max(Integer::compareTo)
                .orElse(0);

        assertEquals(12, highScore);
    }
}
