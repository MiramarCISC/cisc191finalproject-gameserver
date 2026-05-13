package edu.sdccd.cisc191.client.service;

import edu.sdccd.cisc191.client.dto.JoinMatchWebRequest;
import edu.sdccd.cisc191.client.dto.JoinMatchWebResponse;
import edu.sdccd.cisc191.client.dto.MatchHistoryWebResponse;
import edu.sdccd.cisc191.client.dto.PlayMatchWebResponse;
import edu.sdccd.cisc191.grpc.GameServiceGrpc;
import edu.sdccd.cisc191.grpc.JoinMatchRequest;
import edu.sdccd.cisc191.grpc.JoinMatchResponse;
import edu.sdccd.cisc191.grpc.MatchHistoryRequest;
import edu.sdccd.cisc191.grpc.MatchHistoryResponse;
import edu.sdccd.cisc191.grpc.MatchResultResponse;
import edu.sdccd.cisc191.grpc.PlayMatchRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GameGrpcClient {

    private final ManagedChannel channel;
    private final GameServiceGrpc.GameServiceBlockingStub blockingStub;

    public GameGrpcClient(
            @Value("${game.grpc.host:localhost}") String host,
            @Value("${game.grpc.port:50051}") int port
    ) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        this.blockingStub = GameServiceGrpc.newBlockingStub(channel);
    }

    public JoinMatchWebResponse joinMatch(JoinMatchWebRequest webRequest) {
        JoinMatchRequest request = JoinMatchRequest.newBuilder()
                .setPlayerName(safe(webRequest.playerName(), "Player"))
                .setDifficulty(safe(webRequest.difficulty(), "Normal"))
                .setRanked(webRequest.ranked())
                .build();

        JoinMatchResponse response = blockingStub.joinMatch(request);

        return new JoinMatchWebResponse(
                response.getMatchId(),
                response.getPlayerName(),
                response.getOpponentName(),
                response.getDifficulty(),
                response.getRanked(),
                response.getMessage()
        );
    }

    public PlayMatchWebResponse playMatch(String matchId, String playerName) {
        PlayMatchRequest request = PlayMatchRequest.newBuilder()
                .setMatchId(safe(matchId, ""))
                .setPlayerName(safe(playerName, "Player"))
                .build();

        MatchResultResponse response = blockingStub.playMatch(request);

        return new PlayMatchWebResponse(
                response.getMatchId(),
                response.getWinnerName(),
                response.getLoserName(),
                response.getPlayerWon(),
                response.getMessage()
        );
    }

    public MatchHistoryWebResponse loadHistory(String playerName) {
        MatchHistoryRequest request = MatchHistoryRequest.newBuilder()
                .setPlayerName(safe(playerName, "Player"))
                .build();

        MatchHistoryResponse response = blockingStub.loadMatchHistory(request);

        return new MatchHistoryWebResponse(response.getMatchesList());
    }

    private String safe(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value.trim();
    }

    @PreDestroy
    public void shutdown() {
        channel.shutdown();
    }
}
