package com.revo.application.repository;

import com.revo.application.entity.Voting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.HashMap;

@Repository
public class VotingRepositoryImpl implements VotingRepository {
    private final DynamoDbTable<Voting> votingTable;

    @Autowired
    public VotingRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.votingTable = enhancedClient.table(
                Voting.TABLE_NAME,
                TableSchema.fromImmutableClass(Voting.class)
        );
    }

    @Override
    public void store(Voting voting) {
        this.votingTable.putItem(voting);
    }

    @Override
    public Object getListTables() {
        var response = this.votingTable.describeTable();
        var tableDesc = response.table();

        HashMap<String, Object> result = new HashMap<>();
        result.put("table_id", tableDesc.tableId());
        result.put("table_name", tableDesc.tableName());
        result.put("creation_date", tableDesc.creationDateTime());

        return result;
    }

    public void findBetween(String userId, Integer minVoteTime, Integer maxVoteTime) {
        QueryConditional query = QueryConditional.sortBetween(
                b -> b.partitionValue(userId).sortValue(minVoteTime),
                b -> b.partitionValue(userId).sortValue(maxVoteTime)
        );

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(query)
                .build();

        var result = this.votingTable.query(request);

        //soon
        result.forEach(r -> {

        });
    }

    public void findWithStartTime(String userId, Integer minVoteTime) {
        QueryConditional query = QueryConditional.sortGreaterThan(
                b -> b.partitionValue(userId).sortValue(minVoteTime)
        );

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(query)
                .build();

        var result = this.votingTable.query(request);

        //soon
        result.forEach(r -> {

        });
    }

    public void findWithEndTime(String userId, Integer minVoteTime){
        QueryConditional query = QueryConditional.sortLessThan(
                b -> b.partitionValue(userId).sortValue(minVoteTime)
        );

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(query)
                .build();

        var result = this.votingTable.query(request);

        //soon
        result.forEach(r -> {

        });
    }
}
