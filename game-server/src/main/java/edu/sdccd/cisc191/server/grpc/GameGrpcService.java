package edu.sdccd.cisc191.server.grpc;

import edu.sdccd.cisc191.grpc.GameServiceGrpc;
import edu.sdccd.cisc191.grpc.JoinMatchRequest;
import edu.sdccd.cisc191.grpc.JoinMatchResponse;
import edu.sdccd.cisc191.grpc.MatchHistoryRequest;
import edu.sdccd.cisc191.grpc.MatchHistoryResponse;
import edu.sdccd.cisc191.grpc.MatchResultResponse;
import edu.sdccd.cisc191.grpc.PlayMatchRequest;
import edu.sdccd.cisc191.server.entity.MatchEntity;
import edu.sdccd.cisc191.server.repository.MatchRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class GameGrpcService extends GameServiceGrpc.GameServiceImplBase {

    private final MatchRepository matchRepository;
    private final Random random = new Random();

    public GameGrpcService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public void joinMatch(
            JoinMatchRequest request,
            StreamObserver<JoinMatchResponse> responseObserver
    ) {
        String playerName = normalizePlayerName(request.getPlayerName());
        String difficulty = normalizeDifficulty(request.getDifficulty());
        boolean ranked = request.getRanked();

        String matchId = UUID.randomUUID().toString();

        MatchEntity match = new MatchEntity(
                matchId,
                playerName,
                "Bot (" + difficulty + ")",
                difficulty,
                ranked
        );

        matchRepository.save(match);

        JoinMatchResponse response = JoinMatchResponse.newBuilder()
                .setMatchId(match.getMatchId())
                .setPlayerName(match.getPlayerName())
                .setOpponentName(match.getOpponentName())
                .setDifficulty(match.getDifficulty())
                .setRanked(match.isRanked())
                .setMessage("Joined " + match.getMatchType() + " match " + matchId
                        + " on " + difficulty + " difficulty.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void playMatch(
            PlayMatchRequest request,
            StreamObserver<MatchResultResponse> responseObserver
    ) {
        MatchEntity match = matchRepository.findById(request.getMatchId()).orElse(null);

        if (match == null) {
            responseObserver.onNext(MatchResultResponse.newBuilder()
                    .setMatchId(request.getMatchId())
                    .setWinnerName("No winner")
                    .setLoserName("No loser")
                    .setPlayerWon(false)
                    .setMessage("Match not found. Join a match first.")
                    .build());
            responseObserver.onCompleted();
            return;
        }

        if (match.isComplete()) {
            boolean playerWon = match.getWinnerName().equals(match.getPlayerName());
            String loser = playerWon ? match.getOpponentName() : match.getPlayerName();

            responseObserver.onNext(MatchResultResponse.newBuilder()
                    .setMatchId(match.getMatchId())
                    .setWinnerName(match.getWinnerName())
                    .setLoserName(loser)
                    .setPlayerWon(playerWon)
                    .setMessage("Match already completed. Winner: " + match.getWinnerName())
                    .build());
            responseObserver.onCompleted();
            return;
        }

        boolean playerWon = random.nextBoolean();
        String winner = playerWon ? match.getPlayerName() : match.getOpponentName();
        String loser = playerWon ? match.getOpponentName() : match.getPlayerName();

        match.completeWithWinner(winner);
        matchRepository.save(match);

        MatchResultResponse response = MatchResultResponse.newBuilder()
                .setMatchId(match.getMatchId())
                .setWinnerName(winner)
                .setLoserName(loser)
                .setPlayerWon(playerWon)
                .setMessage("Server result: " + winner + " defeated " + loser
                        + " in a " + match.getMatchType() + " "
                        + match.getDifficulty() + " match.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void loadMatchHistory(
            MatchHistoryRequest request,
            StreamObserver<MatchHistoryResponse> responseObserver
    ) {
        String playerName = normalizePlayerName(request.getPlayerName());

        List<MatchEntity> savedMatches =
                matchRepository.findTop10ByPlayerNameOrderByMatchIdDesc(playerName);

        MatchHistoryResponse.Builder response = MatchHistoryResponse.newBuilder();

        if (savedMatches.isEmpty()) {
            response.addMatches(playerName + " has no saved matches yet.");
        } else {
            for (MatchEntity match : savedMatches) {
                String result;

                if (!match.isComplete()) {
                    result = "Pending";
                } else if (match.getWinnerName().equals(match.getPlayerName())) {
                    result = "Win";
                } else {
                    result = "Loss";
                }

                response.addMatches(match.getPlayerName()
                        + " vs " + match.getOpponentName()
                        + " [" + match.getMatchType() + ", " + match.getDifficulty() + "]"
                        + ": " + result);
            }
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    private String normalizePlayerName(String value) {
        if (value == null || value.isBlank()) {
            return "Player";
        }

        return value.trim();
    }

    private String normalizeDifficulty(String value) {
        if (value == null || value.isBlank()) {
            return "Normal";
        }

        return switch (value.trim().toLowerCase()) {
            case "easy" -> "Easy";
            case "hard" -> "Hard";
            default -> "Normal";
        };
    }
}
