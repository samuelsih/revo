package com.revo.application.service;

import com.revo.application.dto.VotingDTO;
import com.revo.application.entity.Status;
import com.revo.application.entity.User;
import com.revo.application.entity.VoteData;
import com.revo.application.entity.Voting;
import com.revo.application.exception.ExpiredVotingTimeException;
import com.revo.application.exception.VotingNotFoundException;
import com.revo.application.gRPC.client.VoteStatusServiceClient;
import com.revo.application.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class VotingServiceImpl implements VotingService {
    private final VotingRepository repository;
    private final VoteStatusServiceClient voteStatusServiceClient;

    @Autowired
    public VotingServiceImpl(VotingRepository repository, VoteStatusServiceClient voteStatusServiceClient) {
        this.repository = repository;
        this.voteStatusServiceClient = voteStatusServiceClient;
    }

    @Override
    public String saveVote(VotingDTO dto, User user) throws ExpiredVotingTimeException, VotingNotFoundException {
        String voteId = dto.getVoteId();
        int position = dto.getChoose();

        VoteData voteData = this.voteStatusServiceClient.checkVoteStatus(voteId, position);
        System.out.println(voteData.status());

        if(voteData.status().equals(Status.EXPIRED)) {
            throw new ExpiredVotingTimeException();
        }

        if(voteData.status().equals(Status.NOT_FOUND)) {
            throw new VotingNotFoundException();
        }

        Voting entity = Voting.builder()
                .userId(user.getUserId())
                .voteId(voteId)
                .voteTo(position)
                .voteAtUnix(Instant.now().getEpochSecond())
                .name(voteData.name())
                .description(voteData.description())
                .imgLink(voteData.imgLink())
                .build();

        this.repository.store(entity);

        return "ok";
    }

    @Override
    public Object getListTables() {
        return this.repository.getListTables();
    }
}
