package com.revo.application.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.revo.application.config.DynamoDBConfig;
import com.revo.application.entity.Voting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VotingRepositoryImpl implements VotingRepository {
    private final DynamoDBConfig db;

    @Autowired
    public VotingRepositoryImpl(DynamoDBConfig db) {
        this.db = db;
    }

    @Override
    public void store(Voting voting) {
        DynamoDBMapper mapper = new DynamoDBMapper(this.db.getInstance());
        mapper.save(voting);
    }

    public Object getListTables() {
        return this.db.getInstance().listTables();
    }
}
