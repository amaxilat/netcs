package org.netcs;


import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration {

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("150.140.5.11:57017");
    }

    @Override
    protected String getDatabaseName() {
        return "netcs";
    }

    @Override
    protected UserCredentials getUserCredentials() {
        return new UserCredentials("netcs", "Bn5ad9T4");
    }
}