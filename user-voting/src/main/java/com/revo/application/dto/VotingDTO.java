package com.revo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.revo.application.validation.OneOf;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;

public class VotingDTO {
    @JsonProperty("vote_id")
    @NotEmpty(message = "unknown vote request")
    @Getter
    private String voteId;

    @JsonProperty("choose")
    @NotEmpty(message = "please choose one to vote")
    @OneOf(allowedValues = {"1", "2", "3", "4"})
    @Getter
    private String choose;
}
