package org.netcs;

import org.apache.log4j.Logger;
import org.netcs.model.mongo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class that executes the experiment.
 */
public class ExperimentExecutor {

    @Autowired
    private MessageSendingOperations<String> messagingTemplate;

    private final ExecutorService executor;
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
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.experiments = new ArrayList<>();
        this.experimentThreads = new ArrayList<>();
    }

    void runExperiment(final String configFileName, final String outputFile, Long nodeCount, final long iterations, final long nodeLimit) throws FileNotFoundException {
        for (Thread thread : experimentThreads) {
            thread.stop();
        }
        experimentThreads.clear();
        experiments.clear();

        int totalCount = 0;
        do {
            for (int i = 0; i < iterations; i++) {
                Experiment experiment = new Experiment(configFileName, outputFile, nodeCount, totalCount++);

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
                executor.submit(thread);
                experimentThreads.add(thread);
            }
            nodeCount += 5;
        } while (nodeCount < nodeLimit);
    }

    public void start(String[] args) throws Exception {
        final String inputFile = args[0];
        final String outputFile = args[1];
        Long nodeCount = null;
        if (args.length > 2) {
            nodeCount = Long.valueOf(args[2]);

        }
        Long iterations = Long.valueOf(args[3]);
        Long nodeLimit = Long.valueOf(args[4]);
        runExperiment(inputFile, outputFile, nodeCount, iterations, nodeLimit);
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
                final Algorithm algo = algorithmRepository.findByName(experiment.getAlgorithmName());
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
                sendWs(experiment);
            }
        }
    }

    private void sendWs(Experiment experiment) {
        final ExperimentResult res = new ExperimentResult();

        res.setId(String.valueOf(experiment.getIndex()));
        res.setSize(String.valueOf(experiment.getExperiment().getPopulationSize()));
        res.setEffectiveInteractions(String.valueOf(experiment.getExperiment().getEffectiveInteractions()));
        res.setInteractions(String.valueOf(experiment.getExperiment().getInteractions()));

        this.messagingTemplate.convertAndSend("/topic/experiments", res);
    }

    class ExperimentResult {
        private String id;
        private String size;
        private String interactions;
        private String effectiveInteractions;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getInteractions() {
            return interactions;
        }

        public void setInteractions(String interactions) {
            this.interactions = interactions;
        }

        public String getEffectiveInteractions() {
            return effectiveInteractions;
        }

        public void setEffectiveInteractions(String effectiveInteractions) {
            this.effectiveInteractions = effectiveInteractions;
        }
    }
}

