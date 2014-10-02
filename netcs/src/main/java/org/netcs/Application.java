package org.netcs;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class that executes the experiment.
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan
@EnableScheduling
@PropertySource("classpath:application.properties")
public class Application
        implements CommandLineRunner {

    @Bean
    ExperimentExecutor experimentExecutor() {
        return new ExperimentExecutor();
    }

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        //PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResourceAsStream("log4j.properties"));

        SpringApplication.run(Application.class, args);

//        final String inputFile = args[0];
//        final String outputFile = args[1];
//        Long nodeCount = null;
//        if (args.length > 2) {
//            nodeCount = Long.valueOf(args[2]);
//        }
//        new Main().runExperiment(inputFile, outputFile, nodeCount);
    }

    @Override
    public void run(String... strings) throws Exception {

    }
}
