package org.netcs;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class that executes the experiment.
 */
public class ExperimentExecutor {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(ExperimentExecutor.class);

    private List<Experiment> experiments;
    private List<Thread> experimentThreads;


    public ExperimentExecutor() {
        this.experiments = new ArrayList<>();
        this.experimentThreads = new ArrayList<>();
    }

    void runExperiment(final String configFileName, final String outputFile, final Long nodeCount) throws FileNotFoundException {
        for (Thread thread : experimentThreads) {
            thread.stop();
        }
        experimentThreads.clear();
        experiments.clear();

        for (int i = 0; i < 1; i++) {
            Experiment experiment = new Experiment(configFileName, outputFile, nodeCount, i);

            experiments.add(experiment);
            Thread thread = new Thread(experiment);
            thread.start();
            experimentThreads.add(thread);
        }
    }

    public void start(String[] args) throws Exception {
        final String inputFile = args[0];
        final String outputFile = args[1];
        Long nodeCount = null;
        if (args.length > 2) {
            nodeCount = Long.valueOf(args[2]);
        }
        runExperiment(inputFile, outputFile, nodeCount);
    }

    public Experiment getExperiment(int i) {
        for (Experiment experiment : experiments) {
            LOGGER.info("experiment:" + experiment);
        }
        return experiments.get(i);
    }
}

