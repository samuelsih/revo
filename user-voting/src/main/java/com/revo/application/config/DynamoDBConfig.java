package com.revo.application.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

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

    private AmazonDynamoDB db;

    private void amazonDynamoDB() {
        ClientConfiguration config = new ClientConfiguration();
        config.setRequestTimeout(1000);
        config.setClientExecutionTimeout(5 * 1000);
        config.setUseTcpKeepAlive(true);

        var customProvider = new AwsCustomCredentialsProvider(accessKey, secretKey);
        var endpointConfig = new AwsClientBuilder.EndpointConfiguration(endpoint, region);

        this.db = AmazonDynamoDBClientBuilder
                .standard()
                .withClientConfiguration(config)
                .withEndpointConfiguration(endpointConfig)
                .withCredentials(customProvider)
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startDynamoDB() {
        log.info("DynamoDB Start On Ready Event!");
        this.amazonDynamoDB();
        log.info("DynamoDB Ready!");
    }

    public AmazonDynamoDB getInstance() {
        return this.db;
    }
}
