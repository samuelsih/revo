package com.revo.application.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GRPCConfig {
    @Value("${grpc.host}")
    public String host;

    @Value("${grpc.port}")
    public Integer port;

    @Bean
    public ManagedChannel getManagedChannel() {
        log.info(String.format("Connecting grpc to %s:%d", this.host, this.port));
        return ManagedChannelBuilder
                .forAddress(this.host, this.port)
                .usePlaintext()
                .build();
    }
}
