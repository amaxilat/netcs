package org.netcs;

import org.apache.log4j.Logger;
import org.netcs.model.mongo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Main class that executes the experiment.
 */
public class ExperimentExecutor {

    @Autowired
    AlgorithmRepository algorithmRepository;
    @Autowired
    AlgorithmStatisticsRepository algorithmStatisticsRepository;

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
            if (configFileName.toLowerCase().contains("line")) {
                experiment.setLookingForLine(true);
            } else if (configFileName.toLowerCase().contains("ring")) {
                experiment.setLookingForCircle(true);
            } else if (configFileName.toLowerCase().contains("star")) {
                experiment.setLookingForStar(true);
            } else if (configFileName.toLowerCase().contains("cycle-cover")) {
                experiment.setLookingForCycleCover(true);
            }
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

    public List<Experiment> getExperiments() {
        return experiments;
    }

    @Scheduled(fixedRate = 1000L)
    public void checker() {
        for (Experiment experiment : experiments) {
            if (experiment.isFinished()) {
                if (experiment.isStored()) continue;
                final Algorithm algo = algorithmRepository.findByName("global-star");
                AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithm(algo);
                if (stats == null) {
                    stats = new AlgorithmStatistics();
                    stats.setAlgorithm(algo);
                    stats.setStatistics(new ArrayList<ExecutionStatistics>());
                    algorithmStatisticsRepository.save(stats);
                }
                final ExecutionStatistics statistics = new ExecutionStatistics();
                statistics.setEffectiveInteractions(experiment.getExperiment().getEffectiveInteractions());
                statistics.setInteractions(experiment.getExperiment().getInteractions());
                statistics.setTime(new Date().getTime());
                statistics.setPopulationSize((long) experiment.getExperiment().getPopulationSize());
                stats.getStatistics().add(statistics);
                algorithmStatisticsRepository.save(stats);

                experiment.setStored(true);
            }
        }
    }
}
