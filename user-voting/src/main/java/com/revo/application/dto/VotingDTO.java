package com.revo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class VotingDTO {
    @JsonProperty("vote_id")
    @NotNull(message = "unknown vote request")
    private String voteId;

    @JsonProperty("choose")
    @NotNull(message = "please choose one to vote")
    @Min(value = 0, message = "invalid choose")
    @Max(value = 3, message = "invalid choose")
    private int choose;
}
