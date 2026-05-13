package edu.sdccd.cisc191.client.controller;

import edu.sdccd.cisc191.client.dto.ErrorWebResponse;
import edu.sdccd.cisc191.client.dto.JoinMatchWebRequest;
import edu.sdccd.cisc191.client.dto.JoinMatchWebResponse;
import edu.sdccd.cisc191.client.dto.MatchHistoryWebResponse;
import edu.sdccd.cisc191.client.dto.PlayMatchWebResponse;
import edu.sdccd.cisc191.client.service.GameGrpcClient;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web/matches")
public class GameWebController {

    private final GameGrpcClient gameGrpcClient;

    public GameWebController(GameGrpcClient gameGrpcClient) {
        this.gameGrpcClient = gameGrpcClient;
    }

    @PostMapping
    public JoinMatchWebResponse joinMatch(@RequestBody JoinMatchWebRequest request) {
        return gameGrpcClient.joinMatch(request);
    }

    @PostMapping("/{matchId}/play")
    public PlayMatchWebResponse playMatch(
            @PathVariable("matchId") String matchId,
            @RequestParam(name = "playerName", defaultValue = "Player") String playerName
    ) {
        return gameGrpcClient.playMatch(matchId, playerName);
    }

    @GetMapping("/history")
    public MatchHistoryWebResponse loadHistory(
            @RequestParam(name = "playerName", defaultValue = "Player") String playerName
    ) {
        return gameGrpcClient.loadHistory(playerName);
    }

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<ErrorWebResponse> handleGrpcError(StatusRuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorWebResponse("Could not reach gRPC server: " + exception.getStatus()));
    }
}