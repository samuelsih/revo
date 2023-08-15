package com.revo.application.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


@Configuration
@Slf4j
public class DynamoDBConfig {
    @Value("${amazon.aws.endpoint}")
    public String endpoint;

    @Value("${amazon.aws.accesskey}")
    public String accessKey;

    @Value("${amazon.aws.secretkey}")
    public String secretKey;

    @Value("${amazon.aws.region}")
    public String region;

    private AwsCredentials getCredentials() {
        return AwsBasicCredentials.create(this.accessKey, this.secretKey);
    }

    private StaticCredentialsProvider getProvider() {
        return StaticCredentialsProvider.create(this.getCredentials());
    }

    private Region getRegion() {
        return Region.of(this.region);
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        log.info("DynamoDB setup...");

        return this.getDynamoDbClient();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        log.info("DynamoDB Enhanced setup...");

        return DynamoDbEnhancedClient
                .builder()
                .dynamoDbClient(this.getDynamoDbClient())
                .build();
    }

    private DynamoDbClient getDynamoDbClient() {
        return DynamoDbClient
                .builder()
                .credentialsProvider(this.getProvider())
                .region(this.getRegion())
                .build();
    }
}
