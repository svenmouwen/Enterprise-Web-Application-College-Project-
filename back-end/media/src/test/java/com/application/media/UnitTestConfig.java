package com.application.media;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.application.media.repository.ExtendedRepositoryImpl;

@Configuration
@SpringJUnitConfig(UnitTestConfig.class)
@EntityScan("com.application.media.model")
@EnableAsync
@ComponentScan(basePackages = "com.application.media")
public abstract class UnitTestConfig {

    }









