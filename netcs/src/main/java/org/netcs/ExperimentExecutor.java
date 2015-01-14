package org.netcs;

import org.apache.log4j.Logger;
import org.netcs.model.mongo.Algorithm;
import org.netcs.model.mongo.AlgorithmStatistics;
import org.netcs.model.mongo.AlgorithmStatisticsRepository;
import org.netcs.model.mongo.ExecutionStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    AlgorithmStatisticsRepository algorithmStatisticsRepository;

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(ExperimentExecutor.class);

    private List<RunnableExperiment> experiments;
    private List<AdvancedRunnableExperiment> advancedExperiments;
    private List<Thread> experimentThreads;
    private List<Thread> advancedExperimentThreads;


    public ExperimentExecutor() {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.experiments = new ArrayList<>();
        this.advancedExperiments = new ArrayList<>();
        this.experimentThreads = new ArrayList<>();
        this.advancedExperimentThreads = new ArrayList<>();
    }

    void runExperiment(final Algorithm algorithm, Long nodeCount, final long iterations, final long nodeLimit) throws FileNotFoundException {
        final String configFileName = algorithm.getName();
        for (final Thread thread : experimentThreads) {
            thread.stop();
        }
        experimentThreads.clear();
        experiments.clear();

        int totalCount = 0;
        do {
            for (int i = 0; i < iterations; i++) {
                RunnableExperiment experiment = new RunnableExperiment(algorithm.getName(), algorithm.getConfigFile(), nodeCount, totalCount++);
                //write statistics to file
                try {
                    final FileWriter fw = new FileWriter("outfile." + i);
                    experiment.setFileWriter(fw);
                } catch (IOException e) {
                    LOGGER.error(e, e);
                }

                experiments.add(experiment);
                if (configFileName.toLowerCase().contains("line")) {
                    experiment.getExperiment().setLookingForLine(true);
                } else if (configFileName.toLowerCase().contains("ring")) {
                    experiment.getExperiment().setLookingForCircle(true);
                } else if (configFileName.toLowerCase().contains("star")) {
                    experiment.getExperiment().setLookingForStar(true);
                } else if (configFileName.toLowerCase().contains("cycle-cover")) {
                    experiment.getExperiment().setLookingForCycleCover(true);
                }
                Thread thread = new Thread(experiment);
                executor.submit(thread);
                experimentThreads.add(thread);
            }
            nodeCount += 5;
        } while (nodeCount < nodeLimit);
    }

    void runExperiment2(final Algorithm algorithm, Long nodeCount, final long iterations, final long nodeLimit) throws FileNotFoundException {
        final String configFileName = algorithm.getName();
        for (final Thread thread : advancedExperimentThreads) {
            thread.stop();
        }
        advancedExperimentThreads.clear();
        advancedExperiments.clear();

        int totalCount = 0;
        do {
            for (int i = 0; i < iterations; i++) {
                AdvancedRunnableExperiment experiment = new AdvancedRunnableExperiment(algorithm.getName(), algorithm.getConfigFile(), nodeCount, totalCount++);
                //write statistics to file
                try {
                    final FileWriter fw = new FileWriter("outfile." + i);
                    experiment.setFileWriter(fw);
                } catch (IOException e) {
                    LOGGER.error(e, e);
                }

                advancedExperiments.add(experiment);

                Thread thread = new Thread(experiment);
                advancedExperimentThreads.add(thread);
                experiment.getExperiment().setLookingForSize(true);
                LOGGER.info("setting Looking for size " + experiment.getExperiment().isLookingForSize());
                executor.submit(thread);
            }
            nodeCount += 10;
        } while (nodeCount < nodeLimit);
    }

    public void start(final Algorithm algorithm, final Long nodeCount, final Long iterations, final Long nodeLimit) throws Exception {
        runExperiment(algorithm, nodeCount, iterations, nodeLimit);
    }

    public void start2(final Algorithm algorithm, final Long nodeCount, final Long iterations, final Long nodeLimit) throws Exception {
        runExperiment2(algorithm, nodeCount, iterations, nodeLimit);
    }


    public RunnableExperiment getExperiment(int i) {
        for (RunnableExperiment experiment : experiments) {
            LOGGER.info("experiment:" + experiment);
        }
        return experiments.get(i);
    }

    public List<RunnableExperiment> getExperiments() {
        return experiments;
    }

    @Scheduled(fixedRate = 1000L)
    public void checker() {
        for (RunnableExperiment experiment : experiments) {
            if (experiment.isFinished()) {
                if (experiment.isStored()) continue;
                AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(experiment.getAlgorithmName());
                if (stats == null) {
                    stats = new AlgorithmStatistics();
                    stats.setAlgorithm(new Algorithm());
                    stats.getAlgorithm().setName(experiment.getAlgorithmName());
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

    @Scheduled(fixedRate = 1000L)
    public void checker2() {
        for (AdvancedRunnableExperiment experiment : advancedExperiments) {
            if (experiment.isFinished()) {
                if (experiment.isStored()) continue;
                AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(experiment.getAlgorithmName());
                if (stats == null) {
                    stats = new AlgorithmStatistics();
                    stats.setAlgorithm(new Algorithm());
                    stats.getAlgorithm().setName(experiment.getAlgorithmName());
                    stats.setStatistics(new ArrayList<ExecutionStatistics>());
                    algorithmStatisticsRepository.save(stats);
                }
                final ExecutionStatistics statistics = new ExecutionStatistics();
                statistics.setEffectiveInteractions(experiment.getExperiment().getEffectiveInteractions());
                statistics.setInteractions(experiment.getExperiment().getInteractions());
                statistics.setTime(new Date().getTime());
                statistics.setPopulationSize((long) experiment.getExperiment().getPopulationSize());
                statistics.setSuccess(experiment.getExperiment().getSuccess());
                statistics.setTerminationMessage(experiment.getExperiment().getTerminationMessage());
                stats.getStatistics().add(statistics);
                algorithmStatisticsRepository.save(stats);

                experiment.setStored(true);
                sendWs(experiment);
            }
        }
    }

    private void sendWs(final RunnableExperiment experiment) {
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

