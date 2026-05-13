package edu.sdccd.cisc191.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "matches")
public class MatchEntity {

    @Id
    private String matchId;

    private String playerName;
    private String opponentName;
    private String difficulty;
    private boolean ranked;
    private boolean complete;
    private String winnerName;

    protected MatchEntity() {
        // Required by JPA.
    }

    public MatchEntity(
            String matchId,
            String playerName,
            String opponentName,
            String difficulty,
            boolean ranked
    ) {
        this.matchId = matchId;
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.difficulty = difficulty;
        this.ranked = ranked;
        this.complete = false;
        this.winnerName = "";
    }

    public String getMatchId() {
        return matchId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public boolean isRanked() {
        return ranked;
    }

    public boolean isComplete() {
        return complete;
    }

    public String getWinnerName() {
        return winnerName == null ? "" : winnerName;
    }

    public String getMatchType() {
        return ranked ? "ranked" : "casual";
    }

    public void completeWithWinner(String winnerName) {
        this.winnerName = winnerName;
        this.complete = true;
    }
}
