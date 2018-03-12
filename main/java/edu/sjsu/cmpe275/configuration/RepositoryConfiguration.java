package edu.sjsu.cmpe275.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"edu.sjsu.cmpe275.domain"})
@EnableJpaRepositories(basePackages = {"edu.sjsu.cmpe275.repository"})
@EnableTransactionManagement
public class RepositoryConfiguration {
}

