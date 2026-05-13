package edu.sdccd.cisc191.client.dto;

import java.util.List;

public record MatchHistoryWebResponse(
        List<String> matches
) {
}
