package edu.sdccd.cisc191.client.dto;

public record JoinMatchWebResponse(
        String matchId,
        String playerName,
        String opponentName,
        String difficulty,
        boolean ranked,
        String message
) {
}
