package org.netcs;

import org.apache.log4j.Logger;
import org.netcs.model.mongo.Algorithm;
import org.netcs.model.mongo.AlgorithmStatistics;
import org.netcs.model.mongo.AlgorithmStatisticsRepository;
import org.netcs.model.mongo.ExecutionStatistics;
import org.netcs.model.sql.Experiment;
import org.netcs.model.sql.ExperimentRepository;
import org.netcs.model.sql.TerminationStat;
import org.netcs.model.sql.TerminationStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
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
    @Autowired
    ExperimentRepository experimentRepository;
    @Autowired
    TerminationStatRepository terminationStatRepository;
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(ExperimentExecutor.class);

    private List<RunnableExperiment> experiments;

    @Value("${mine.simple:false}")
    String mineSimple;
    @Value("${mine.simple.name:}")
    String mineSimpleName;
    @Value("${mine.simple.scheduler:}")
    String mineSimpleScheduler;
    @Value("${mine.simple.threshold:}")
    long mineSimpleThreshold;
    @Value("${mine.count:false}")
    String mineCount;
    @Value("${mine.advanced:false}")
    String mineAdvanced;

    private List<AdvancedRunnableExperiment> advancedExperiments;
    private List<Thread> experimentThreads;
    private List<Thread> advancedExperimentThreads;
    private int executors;
    private Long count;
    private int totalCount;
    private int randomIndex;


    public ExperimentExecutor() {
//        this.executors = Runtime.getRuntime().availableProcessors() > 2 ? Runtime.getRuntime().availableProcessors() - 2 : 1;
        this.executors = Runtime.getRuntime().availableProcessors() / 2;
//        this.executors = Runtime.getRuntime().availableProcessors();
//        this.executors = 1;

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


//        final List<AlgorithmStatistics> algos = algorithmStatisticsRepository.findAll();
//        for (AlgorithmStatistics algo : algos) {
//            for (ExecutionStatistics statistics : algo.getStatistics()) {
//                Experiment experiment = new Experiment();
//                experiment.setAlgorithm(algo.getAlgorithm().getName());
//                experiment.setEffectiveInteractions(statistics.getEffectiveInteractions());
//                experiment.setInteractions(statistics.getInteractions());
//                experiment.setPopulationSize(statistics.getPopulationSize());
//                experiment.setScheduler(statistics.getScheduler());
//                experiment.setSuccess(statistics.isSuccess());
//                experiment.setTerminationMessage(statistics.getTerminationMessage());
//                experiment.setTime(statistics.getTime());
//                experimentRepository.save(experiment);
//                Map<String, String> stats = statistics.getTerminationStats();
//                for (String s : stats.keySet()) {
//                    TerminationStat stat = new TerminationStat();
//                    stat.setExperiment(experiment);
//                    stat.setName(s);
//                    stat.setData(stats.get(s));
//                    terminationStatRepository.save(stat);
//                }
//
//            }
//        }

//
//
//        LOGGER.info(algo);
//        for (ExecutionStatistics executionStatistics : algo.getStatistics()) {
//            if (executionStatistics.getTerminationStats() == null) {
//                executionStatistics.setTerminationStats(new HashMap<String, String>());
//            }
//            if (executionStatistics.getInteractions() != null) {
//                executionStatistics.getTerminationStats().put("interactions", String.valueOf(executionStatistics.getInteractions()));
//            }
//            if (executionStatistics.getEffectiveInteractions() != null) {
//                executionStatistics.getTerminationStats().put("effectiveInteractions ", String.valueOf(executionStatistics.getEffectiveInteractions()));
//            }
//            if (executionStatistics.getPopulationSize() != null) {
//                executionStatistics.getTerminationStats().put("populationSize", String.valueOf(executionStatistics.getPopulationSize()));
//            }
//        }
//        algorithmStatisticsRepository.save(algo);


//        this.count = lookupService.getCount("counter");
//        if (count == null) {
//            lookupService.setCount("counter", 110L);
//            this.count = lookupService.getCount("counter");
//        }
        
//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.exit(1);
//            }
//        }, 60 * 60 * 1000);
        count = 100L;
    }

    void runExperiment(final Algorithm algorithm, Long nodeCount, long index) throws FileNotFoundException {
        runExperiment(algorithm, "Random", nodeCount, index);
    }

    void runExperiment(final Algorithm algorithm, final String scheduler, Long nodeCount, long index) throws FileNotFoundException {
        final String configFileName = algorithm.getName();

        RunnableExperiment experiment = new RunnableExperiment(algorithm.getName(), scheduler, algorithm.getConfigFile(), nodeCount, index, messagingTemplate, lookupService);
        //write statistics to file
        try {
            final FileWriter fw = new FileWriter("outfile." + index);
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

    void runExperiments(final Algorithm algorithm, Long nodeCount, final long iterations, final long nodeLimit) throws FileNotFoundException {
        runExperiments(algorithm, nodeCount, iterations, nodeLimit, "Random");
    }

    void runExperiments(final Algorithm algorithm, Long nodeCount, final long iterations, final long nodeLimit, final String scheduler) throws FileNotFoundException {
        do {
            for (int i = 0; i < iterations; i++) {
                runExperiment(algorithm, scheduler, nodeCount, i);
            }
            nodeCount += 50;
        } while (nodeCount < nodeLimit);
    }

    void runSimpleAlgorithmExperimentsInBackground(final String algorithmName) {


        if (executor.getActiveCount() < executors && (executor.getTaskCount() - executor.getCompletedTaskCount()) < 2 * executors) {
            LOGGER.info("Checking experiments for " + count + " nodes...");
            final AlgorithmStatistics algoStatistics = algorithmStatisticsRepository.findByAlgorithmName(algorithmName);

            final Algorithm algo = algoStatistics.getAlgorithm();
            int results = 0;
            for (final ExecutionStatistics executionStatistics : algoStatistics.getStatistics()) {
                if (count.equals(Long.parseLong(executionStatistics.getTerminationStats().get("populationSize")))
                        && mineSimpleScheduler.equals(executionStatistics.getScheduler())
//                        && executionStatistics.getTerminationMessage().contains("b=1,")
                        ) {
                    results++;
                }
            }

            LOGGER.info("Found " + results + " experiments for " + algo.getName() + " under:" + mineSimpleScheduler + ".");
            if (results < mineSimpleThreshold) {
                try {
                    LOGGER.info("Adding " + algo.getName() + " experiment for " + count + " nodes.");
                    runExperiments(algo, count, executors, count, mineSimpleScheduler);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//                addRunnableExperiment(algo, count);
            } else {
                long step = 100;
                if (count >= 1000) {
                    step = 500;
                }
                final long next = count + step;

                LOGGER.info("Enough experiments executed for " + count + " nodes, increasing count to " + next + " nodes.");
                count = next;
                lookupService.setCount(algo.getName(), count);
            }
        }
    }

    //    @Scheduled(fixedRate = 500L)
    void runRandomExperimentsInBackground() {
        LOGGER.info("Checking experiments for " + count + " nodes...");
        final AlgorithmStatistics algoStatistics = algorithmStatisticsRepository.findByAlgorithmName("fast-global-line-30");
        final Algorithm algo = algoStatistics.getAlgorithm();
        int results = 0;
        for (final ExecutionStatistics executionStatistics : algoStatistics.getStatistics()) {
            if (executionStatistics.getPopulationSize().equals(count)) {
                results++;
            }
        }

        LOGGER.info("Found " + results + " experiments for " + algo.getName() + ".");
        if (results < 100) {
            LOGGER.info("Adding " + algo.getName() + " experiment for " + count + " nodes.");
            addRunnableExperiment(algo, count);
        } else {
            final long next = count + 10;
            LOGGER.info("Enough experiments executed for " + count + " nodes, increasing count to " + next + " nodes.");
            count = next;
        }
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

    void runAdvancedExperiment(final Algorithm algorithm, Long startNodeCount, final long iterations, final long nodeLimit) throws FileNotFoundException {
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
        runExperiments(algorithm, nodeCount, iterations, nodeLimit);
    }

    public void start2(final Algorithm algorithm, final Long nodeCount, final Long iterations, final Long nodeLimit) throws Exception {
        runAdvancedExperiment(algorithm, nodeCount, iterations, nodeLimit);
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
                LOGGER.info("Storing under " + experiment.getScheduler().getClass().getSimpleName() + " scheduler...");

                final ExecutionStatistics statistics = new ExecutionStatistics();

                statistics.setEffectiveInteractions(experiment.getExperiment().getEffectiveInteractions());
                statistics.setInteractions(experiment.getExperiment().getInteractions());
                statistics.setTime(new Date().getTime());
                statistics.setPopulationSize((long) experiment.getExperiment().getPopulationSize());

                statistics.setScheduler(experiment.getScheduler().getClass().getSimpleName());


                statistics.setTerminationStats(experiment.getExperiment().getTerminationStats());
                statistics.getTerminationStats().put("interactions", String.valueOf(statistics.getInteractions()));
                statistics.getTerminationStats().put("effectiveInteractions", String.valueOf(statistics.getEffectiveInteractions()));
                statistics.getTerminationStats().put("populationSize", String.valueOf(statistics.getPopulationSize()));

                if (experiment.getExperiment().getTerminationStats().containsKey("time")) {
                    statistics.getTerminationStats().put("time", String.valueOf(experiment.getExperiment().getTerminationStats().get("time")));
                }
                stats.getStatistics().add(statistics);
                algorithmStatisticsRepository.save(stats);
                LOGGER.info("Experiment:" + experiment.getIndex() + " SAVED");

                storeSQL(experiment);

                experiment.setStored(true);
                sendWs(experiment);
            }
        }
        if ("true".equals(mineSimple) && mineSimpleName != null)
            runSimpleAlgorithmExperimentsInBackground(mineSimpleName);
    }

    private void storeSQL(RunnableExperiment experiment) {

        LOGGER.info("Experiment:" + experiment.getIndex() + " Finished:" + experiment.isFinished() + " Stored:" + experiment.isStored());

        Experiment experimentSql = new Experiment();
        LOGGER.info("Storing under " + experiment.getScheduler().getClass().getSimpleName() + " scheduler...");

        experimentSql.setAlgorithm(experiment.getAlgorithmName());
        experimentSql.setPopulationSize(experiment.getNodeCount());
        experimentSql.setInteractions(experiment.getExperiment().getInteractions());
        experimentSql.setEffectiveInteractions(experiment.getExperiment().getEffectiveInteractions());
        experimentSql.setSuccess(true);
        experimentSql.setTime(System.currentTimeMillis());
        experimentSql.setScheduler(experiment.getScheduler().getClass().getSimpleName());

        experimentRepository.save(experimentSql);
        Map<String, String> statistics = experiment.getExperiment().getTerminationStats();

        List<TerminationStat> terminationStats = new ArrayList<>();

        terminationStats.add(addStat(experimentSql, "interactions", statistics.get("interactions")));
        terminationStats.add(addStat(experimentSql, "effectiveInteractions", statistics.get("effectiveInteractions")));
        terminationStats.add(addStat(experimentSql, "populationSize", statistics.get("populationSize")));


        if (experiment.getExperiment().getTerminationStats().containsKey("time")) {
            terminationStats.add(addStat(experimentSql, "populationSize", statistics.get("time")));
        }
        terminationStatRepository.save(terminationStats);

        LOGGER.info("Experiment:" + experiment.getIndex() + " SAVED");

    }

    private TerminationStat addStat(final Experiment experiment, final String name, final String data) {
        TerminationStat interactionsStat = new TerminationStat();
        interactionsStat.setName(name);
        interactionsStat.setData(data);
        interactionsStat.setId(experiment.getId());
        return interactionsStat;
    }

    //@Scheduled(fixedRate = 100L)
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

