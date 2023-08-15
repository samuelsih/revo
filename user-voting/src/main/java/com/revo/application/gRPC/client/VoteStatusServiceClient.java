package com.revo.application.gRPC.client;

import com.revo.application.entity.VoteData;

public interface VoteStatusServiceClient {
    VoteData checkVoteStatus(String voteId, int position);
}
