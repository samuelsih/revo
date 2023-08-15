package com.revo.application.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbImmutable(builder = Voting.Builder.class)
public class Voting {
    public static final String TABLE_NAME = "vote_table";

    private final String userId;
    private final long voteAtUnix;
    private final String voteTo;
    private final String voteId;
    private final String name;
    private final String description;
    private final String imgLink;

    private Voting(Builder b) {
        this.userId = b.userId;
        this.voteAtUnix = b.voteAtUnix;
        this.voteTo = b.voteTo;
        this.name = b.name;
        this.description = b.description;
        this.imgLink = b.imgLink;
        this.voteId = b.voteId;
    }

    public static Builder builder() {
        return new Builder();
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("user_id")
    public String userId() {
        return this.userId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("vote_at_unix")
    public long voteAtUnix() {
        return this.voteAtUnix;
    }

    @DynamoDbAttribute("name")
    public String name() {
        return this.name;
    }

    @DynamoDbAttribute("description")
    public String description() {
        return this.description;
    }

    @DynamoDbAttribute("img_link")
    public String imgLink() {
        return this.imgLink;
    }

    @DynamoDbAttribute("vote_to")
    public String voteTo() {
        return this.voteTo;
    }

    @DynamoDbAttribute("vote_id")
    public String voteId() {
        return this.voteId;
    }

    public static final class Builder {
        private String userId;
        private long voteAtUnix;
        private String voteTo;
        private String name;
        private String description;
        private String imgLink;
        private String voteId;

        private Builder() {}

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder voteAtUnix(long voteAtUnix) {
            this.voteAtUnix = voteAtUnix;
            return this;
        }

        public Builder voteTo(String voteTo) {
            this.voteTo = voteTo;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder imgLink(String imgLink) {
            this.imgLink = imgLink;
            return this;
        }

        public Builder voteId(String voteId) {
            this.voteId = voteId;
            return this;
        }

        public Voting build() {
            return new Voting(this);
        }
    }
}
