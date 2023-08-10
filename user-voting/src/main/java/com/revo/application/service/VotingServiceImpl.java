package com.revo.application.service;

import com.revo.application.entity.Voting;
import com.revo.application.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotingServiceImpl implements VotingService {
    private final VotingRepository repository;

    @Autowired
    public VotingServiceImpl(VotingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveVote(Voting voting) {
        this.repository.store(voting);
    }

    @Override
    public Object getListTables() {
        return this.repository.getListTables();
    }
}
