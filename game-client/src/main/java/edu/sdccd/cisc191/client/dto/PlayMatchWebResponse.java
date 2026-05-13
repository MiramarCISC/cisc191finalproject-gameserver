package edu.sdccd.cisc191.client.dto;

public record PlayMatchWebResponse(
        String matchId,
        String winnerName,
        String loserName,
        boolean playerWon,
        String message
) {
}
