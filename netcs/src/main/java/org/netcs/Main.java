package org.netcs;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main class that executes the experiment.
 */
@ComponentScan
@EnableAutoConfiguration
public class Main {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        //PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResourceAsStream("log4j.properties"));

        SpringApplication.run(Main.class, args);

        ExperimentExecutor.getInstance().start(args);
//        final String inputFile = args[0];
//        final String outputFile = args[1];
//        Long nodeCount = null;
//        if (args.length > 2) {
//            nodeCount = Long.valueOf(args[2]);
//        }
//        new Main().runExperiment(inputFile, outputFile, nodeCount);
    }
}
