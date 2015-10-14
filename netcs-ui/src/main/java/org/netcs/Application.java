package org.netcs;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class that executes the experiment.
 */
@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
public class Application
        implements CommandLineRunner {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        LOGGER.debug("Started!");
    }

    @Override
    public void run(String... strings) throws Exception {

    }
}
