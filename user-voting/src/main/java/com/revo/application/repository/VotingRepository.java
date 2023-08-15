package com.revo.application.repository;

import com.revo.application.entity.Voting;

public interface VotingRepository {
    void store(Voting voting);
    Object getListTables();
}
