package com.revo.application.gRPC.client;

import com.revo.application.entity.Status;
import com.revo.application.entity.VoteData;
import com.revo.application.gRPC.protogen.Request;
import com.revo.application.gRPC.protogen.Response;
import com.revo.application.gRPC.protogen.VoteStatusServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoteStatusServiceClientImpl implements VoteStatusServiceClient {
    private final VoteStatusServiceGrpc.VoteStatusServiceBlockingStub stub;

    @Autowired
    public VoteStatusServiceClientImpl(ManagedChannel channel) {
        this.stub = VoteStatusServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public VoteData checkVoteStatus(String voteId, int position) {
        Request request = Request.newBuilder()
                .setVoteId(voteId)
                .setCandidatePosition(position)
                .build();

        Response response = stub.checkStatus(request);

        return new VoteData(
               Status.valueOf(response.getStatus()),
               response.getName(),
               response.getDescription(),
               response.getImgLink()
        );
    }
}
