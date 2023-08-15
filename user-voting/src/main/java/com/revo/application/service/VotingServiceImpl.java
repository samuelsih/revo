package com.revo.application.service;

import com.revo.application.dto.VotingDTO;
import com.revo.application.entity.User;
import com.revo.application.entity.Voting;
import com.revo.application.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class VotingServiceImpl implements VotingService {
    private final VotingRepository repository;

    @Autowired
    public VotingServiceImpl(VotingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveVote(VotingDTO dto, User user) {
        Voting entity = Voting.builder()
                .userId(user.getUserId())
                .voteId(dto.getVoteId())
                .voteTo(dto.getChoose())
                .voteAtUnix(Instant.now().getEpochSecond())
                .name("test")
                .description("description test")
                .imgLink("https://google.com")
                .build();

        this.repository.store(entity);
    }

    @Override
    public Object getListTables() {
        return this.repository.getListTables();
    }
}
