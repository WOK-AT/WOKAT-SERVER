package com.sopt.wokat.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableTransactionManagement 
@EnableMongoRepositories(basePackages = "com.sopt.wokat")
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }
    
    @Override
    protected String getDatabaseName() {
        return "wokat";
    }
    
    @Bean
    public MongoTransactionManager mongoTransactionManager() {
        return new MongoTransactionManager(mongoDbFactory());
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
        return mongoTemplate;
    }

}
