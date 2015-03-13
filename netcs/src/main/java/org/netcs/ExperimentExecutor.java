package org.netcs;

import org.apache.log4j.Logger;
import org.netcs.model.mongo.Algorithm;
import org.netcs.model.mongo.AlgorithmStatistics;
import org.netcs.model.mongo.AlgorithmStatisticsRepository;
import org.netcs.model.mongo.ExecutionStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Main class that executes the experiment.
 */
public class ExperimentExecutor {

    @Autowired
    private MessageSendingOperations<String> messagingTemplate;
    private final ThreadPoolExecutor executor;

    @Autowired
    AlgorithmStatisticsRepository algorithmStatisticsRepository;
    @Autowired
    LookupService lookupService;
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(ExperimentExecutor.class);

    private List<RunnableExperiment> experiments;

    private List<AdvancedRunnableExperiment> advancedExperiments;
    private List<Thread> experimentThreads;
    private List<Thread> advancedExperimentThreads;
    private final int executors;
    private Long count;
    private int totalCount;
    private int randomIndex;


    public ExperimentExecutor() {
//        this.executors = Runtime.getRuntime().availableProcessors() > 2 ? Runtime.getRuntime().availableProcessors() - 2 : 1;
        this.executors = Runtime.getRuntime().availableProcessors() - 2;

        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(executors);
        this.experiments = new ArrayList<>();
        this.advancedExperiments = new ArrayList<>();
        this.experimentThreads = new ArrayList<>();
        this.advancedExperimentThreads = new ArrayList<>();
        this.totalCount = 0;
        this.randomIndex = 0;
    }

    @PostConstruct
    public void init() {
//        this.count = lookupService.getCount("counter");
//        if (count == null) {
//            lookupService.setCount("counter", 110L);
//            this.count = lookupService.getCount("counter");
//        }
        count = 100L;
    }

    void runExperiment(final Algorithm algorithm, Long nodeCount, final long iterations, final long nodeLimit) throws FileNotFoundException {
        final String configFileName = algorithm.getName();
        for (final Thread thread : experimentThreads) {
            thread.stop();
        }
        experimentThreads.clear();
        experiments.clear();


        do {
            for (int i = 0; i < iterations; i++) {
                RunnableExperiment experiment = new RunnableExperiment(algorithm.getName(), algorithm.getConfigFile(), nodeCount, totalCount++, messagingTemplate, lookupService);
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
            nodeCount += 10;
        } while (nodeCount < nodeLimit);
    }

    //    @Scheduled(fixedRate = 500L)
    void runExperimentsInBackground() {
        if (executor.getActiveCount() < executors && (executor.getTaskCount() - executor.getCompletedTaskCount()) < 2 * executors) {
            LOGGER.info("Checking experiments for " + count + " nodes...");
            final AlgorithmStatistics algoStatistics = algorithmStatisticsRepository.findByAlgorithmName("counter");
            final Algorithm algo = algoStatistics.getAlgorithm();
            int results = 0;
            for (final ExecutionStatistics executionStatistics : algoStatistics.getStatistics()) {
                if (count.equals(executionStatistics.getPopulationSize()) && executionStatistics.getTerminationMessage().contains("b=1,")) {
                    results++;
                }
            }

            LOGGER.info("Found " + results + " experiments for " + algo.getName() + ".");
            if (results < count) {
                LOGGER.info("Adding " + algo.getName() + " experiment for " + count + " nodes.");
                addAdvancedRunnableExperiment(algo, count);
            } else {
                LOGGER.info("Enough experiments executed for " + count + " nodes, increasing count to " + (count + 10) + " nodes.");
                count += 10;
                lookupService.setCount(algo.getName(), count);
            }
        }
    }

    @Scheduled(fixedRate = 10000L)
    void runRandomExperimentsInBackground() {
        LOGGER.info("Checking experiments for " + count + " nodes...");
        randomIndex = randomIndex % 4;
        final AlgorithmStatistics algoStatistics = algorithmStatisticsRepository.findByAlgorithmName("random" + randomIndex);
        final Algorithm algo = algoStatistics.getAlgorithm();
        int results = 0;
        for (final ExecutionStatistics executionStatistics : algoStatistics.getStatistics()) {
            if (executionStatistics.getPopulationSize().equals(count)) {
                results++;
            }
        }

        LOGGER.info("Found " + results + " experiments for " + algo.getName() + ".");
        if (results < (2*count)/10) {
            LOGGER.info("Adding " + algo.getName() + " experiment for " + count + " nodes.");
            addRunnableExperiment(algo, count);
        } else {
            LOGGER.info("Enough experiments executed for " + count + " nodes, increasing count to " + (count + 10) + " nodes.");
            count += 10;
        }
        randomIndex++;
    }

    void addRunnableExperiment(final Algorithm algorithm, Long count) {

        final String configFileName = algorithm.getName();

        RunnableExperiment experiment = new RunnableExperiment(algorithm.getName(), algorithm.getConfigFile(), count, totalCount++, messagingTemplate, lookupService);
        //write statistics to file
        try {
            final FileWriter fw = new FileWriter("outfile." + totalCount);
            experiment.setFileWriter(fw);
        } catch (IOException e) {
            LOGGER.error(e, e);
        }

        experiments.add(experiment);

        Thread thread = new Thread(experiment);
        experimentThreads.add(thread);
        LOGGER.info("setting Looking for size " + experiment.getExperiment().isLookingForSize());
        executor.submit(thread);
    }

    void addAdvancedRunnableExperiment(final Algorithm algorithm, Long count) {

        final String configFileName = algorithm.getName();

        AdvancedRunnableExperiment experiment = new AdvancedRunnableExperiment(algorithm.getName(), algorithm.getConfigFile(), count, totalCount++, messagingTemplate, lookupService);
        //write statistics to file
        try {
            final FileWriter fw = new FileWriter("outfile." + totalCount);
            experiment.setFileWriter(fw);
        } catch (IOException e) {
            LOGGER.error(e, e);
        }

        advancedExperiments.add(experiment);

        Thread thread = new Thread(experiment);
        advancedExperimentThreads.add(thread);
        LOGGER.info("setting Looking for size " + experiment.getExperiment().isLookingForSize());
        executor.submit(thread);
    }

    void runExperiment2(final Algorithm algorithm, Long startNodeCount, final long iterations, final long nodeLimit) throws FileNotFoundException {
        final String configFileName = algorithm.getName();
        for (final Thread thread : advancedExperimentThreads) {
            thread.stop();
        }
        advancedExperimentThreads.clear();
        advancedExperiments.clear();

        for (int i = 0; i < iterations; i++) {
            for (Long nodeCount = startNodeCount; nodeCount < nodeLimit; nodeCount += 10) {
                AdvancedRunnableExperiment experiment = new AdvancedRunnableExperiment(algorithm.getName(), algorithm.getConfigFile(), nodeCount, totalCount++, messagingTemplate, lookupService);
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
        }
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

    public List<AdvancedRunnableExperiment> getAdvancedExperiments() {
        return advancedExperiments;
    }

    @Scheduled(fixedRate = 1000L)
    public void checker() {
        for (RunnableExperiment experiment : experiments) {
            if (experiment.isFinished()) {
                LOGGER.info("Experiment:" + experiment.getIndex() + " Finished:" + experiment.isFinished() + " Stored:" + experiment.isStored());
                if (experiment.isStored()) {
                    experiments.remove(experiment);
                    return;
                }
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

                statistics.setTerminationStats(experiment.getExperiment().getTerminationStats());
                statistics.getTerminationStats().put("interactions", String.valueOf(statistics.getInteractions()));
                statistics.getTerminationStats().put("effectiveInteractions", String.valueOf(statistics.getEffectiveInteractions()));
                statistics.getTerminationStats().put("populationSize", String.valueOf(statistics.getPopulationSize()));
                stats.getStatistics().add(statistics);
                algorithmStatisticsRepository.save(stats);
                LOGGER.info("Experiment:" + experiment.getIndex() + " SAVED");

                experiment.setStored(true);
                sendWs(experiment);
            }
        }
    }

    @Scheduled(fixedRate = 100L)
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
                advancedExperiments.remove(experiment);
                return;
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

