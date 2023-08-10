package com.revo.application.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicReference;

// Solve DynamoDB slow first request issue
@Slf4j
public class AwsCustomCredentialsProvider implements AWSCredentialsProvider {
    private final AWSStaticCredentialsProvider awsCredentialsProvider;
    private static final AtomicReference<AWSCredentials> cachedAwsCredentials = new AtomicReference<>();

    public AwsCustomCredentialsProvider(String accessKey, String secretKey) {
        this.awsCredentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
    }

    @Override
    public AWSCredentials getCredentials() {
        return cachedAwsCredentials.get() != null ? cachedAwsCredentials.get() : fetchAndUpdateCredentials();
    }

    @Override
    public void refresh() {
        fetchAndUpdateCredentials();
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    private AWSCredentials fetchAndUpdateCredentials() {
        long start = System.currentTimeMillis();
        log.info("Refreshing AWS Credentials....");
        try {
            cachedAwsCredentials.set(awsCredentialsProvider.getCredentials());
            log.info("Refreshed AWS Credentials successfully in : {} ms", System.currentTimeMillis() - start);
        } catch (Throwable th) {
            log.error("fetchAndUpdateCredentials :: Exception occurred", th);
        }

        return cachedAwsCredentials.get();
    }
}
