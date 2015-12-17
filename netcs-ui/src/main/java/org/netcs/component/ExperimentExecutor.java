package org.netcs.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.netcs.RunnableExperiment;
import org.netcs.model.mongo.Algorithm;
import org.netcs.model.mongo.AlgorithmStatistics;
import org.netcs.model.mongo.AlgorithmStatisticsRepository;
import org.netcs.model.mongo.ExecutionStatistics;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;
import org.netcs.model.sql.*;
import org.netcs.service.LookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Main class that executes the experiment.
 */
@Component
public class ExperimentExecutor {

    @Autowired
    private MessageSendingOperations<String> messagingTemplate;

    @Autowired
    AlgorithmStatisticsRepository algorithmStatisticsRepository;
    @Autowired
    LookupService lookupService;
    @Autowired
    ExperimentRepository experimentRepository;
    @Autowired
    TerminationStatRepository terminationStatRepository;
    @Autowired
    TerminationConditionRepository terminationConditionRepository;
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(ExperimentExecutor.class);

    public static int MAX_CONCURENT_EXPERIMENTS = 2;
    @Value("${mine.simple.name:}")
    public String mineSimpleName;
    @Value("${mine.simple.scheduler:}")
    public String mineSimpleScheduler;
    @Value("${mine.simple:false}")
    String mineSimple;
    @Value("${mine.simple.threshold:}")
    public long mineSimpleThreshold;
    @Value("${mine.count:false}")
    String mineCount;
    @Value("${mine.advanced:false}")
    String mineAdvanced;


    private ConcurrentLinkedQueue<Future<RunnableExperiment>> futureExperiments;
    private Map<Long, RunnableExperiment> experimentMap;
    private ExecutorService pool;

    public Long count;
    private long index;


    @PostConstruct
    public void init() {
        count = 100L;
        index = 1;
        futureExperiments = new ConcurrentLinkedQueue<>();
        experimentMap = new HashMap<>();
        pool = Executors.newCachedThreadPool();
    }


    private void scheduleExperiement(final String algorithmName) {
        Long count = 100L;
        boolean scheduled = false;
        ArrayList<String> schedulers = new ArrayList<>();
        for (String s : mineSimpleScheduler.split(",")) {
            schedulers.add(s);
        }
        do {
            LOGGER.info("Checking experiments for " + count + " nodes...");
            final AlgorithmStatistics algoStatistics = algorithmStatisticsRepository.findByAlgorithmName(algorithmName);

            final Algorithm algo = algoStatistics.getAlgorithm();

            for (String scheduler : schedulers) {
                Long existingCount = experimentRepository.findExperimentsForAlgorithmAndScheduler(algorithmName, scheduler, count);
                LOGGER.info("Found " + existingCount + " experiments for " + algo.getName() + " under:" + scheduler + ".");
                if (existingCount < mineSimpleThreshold) {
                    try {
                        LOGGER.info("Adding " + algo.getName() + " experiment for " + count + " nodes.");
                        runExperiment(algo, scheduler, count, index++);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    scheduled = true;
                    break;
                }
            }

            if (!scheduled) {
                long step = 100;
                if (count >= 1000) {
                    step = 500;
                }
                final long next = count + step;

                LOGGER.info("Enough experiments executed for " + count + " nodes, increasing count to " + next + " nodes.");
                count = next;
                lookupService.setCount(algo.getName(), count);
            }
        } while (!scheduled);
    }


    public void runExperiment(final Algorithm algorithm, final String scheduler, Long nodeCount) throws FileNotFoundException {
        runExperiment(algorithm, scheduler, nodeCount, index++);
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

        if (configFileName.toLowerCase().contains("line")) {
            experiment.getExperiment().setLookingForLine(true);
        } else if (configFileName.toLowerCase().contains("ring")) {
            experiment.getExperiment().setLookingForCircle(true);
        } else if (configFileName.toLowerCase().contains("star")) {
            experiment.getExperiment().setLookingForStar(true);
        } else if (configFileName.toLowerCase().contains("cycle-cover")) {
            experiment.getExperiment().setLookingForCycleCover(true);
        }

        try {
            futureExperiments.add(startExperiment(experiment));
            experimentMap.put(experiment.getIndex(), experiment);
        } catch (IOException e) {
            LOGGER.error(e, e);
        }

    }


    public Future<RunnableExperiment> startExperiment(final RunnableExperiment experiment) throws IOException {
        return pool.submit(new Callable<RunnableExperiment>() {
            @Override
            public RunnableExperiment call() throws Exception {
                experiment.run();
                return experiment;
            }
        });
    }


    @Scheduled(fixedRate = 1000L)
    public void checkForExperimentsDone() {

        int runningCount = 0;
        int totalSize = futureExperiments.size();
        for (final Future<RunnableExperiment> futureExperiment : futureExperiments) {
            if (futureExperiment.isDone()) {
                try {
                    //get the result
                    RunnableExperiment experiment = futureExperiment.get();
                    //store the results
                    storeStatistics(experiment);
                    //remove it from the list
                    futureExperiments.remove(futureExperiment);
                    experimentMap.remove(experiment.getIndex());
                    long allocatedMemoryBefore = Runtime.getRuntime().totalMemory();
                    System.gc();
                    long allocatedMemoryAfter = Runtime.getRuntime().totalMemory();
                    LOGGER.info("Freed " + (allocatedMemoryBefore - allocatedMemoryAfter) / 1024 + "kb");
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.error(e, e);
                }
            } else {
                runningCount++;
                LOGGER.debug("Experiment " + futureExperiment + " is running...");
            }
        }
        LOGGER.info(runningCount + "/" + totalSize + " experiments still running...");

        while (mineSimple.equals("true") && futureExperiments.size() < MAX_CONCURENT_EXPERIMENTS) {
            scheduleExperiement(mineSimpleName);
        }
    }

    private void storeStatistics(RunnableExperiment experiment) {
        LOGGER.info("Experiment:" + experiment.getIndex() + " Finished:" + experiment.isFinished() + " Storing...");
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

        try {
            Experiment expSql = storeSQL(experiment);
            TerminationCondition condition = new TerminationCondition();
            condition.setExperiment(expSql);
            try {

                final List<CNode> cnodes = new ArrayList<>();
                for (final PopulationNode node : experiment.getExperiment().getPopulation().getNodes()) {
                    cnodes.add(new CNode(node.getNodeName(), node.getState()));
                }
                final String nodesString = new ObjectMapper().writeValueAsString(cnodes);
                condition.setNodes(nodesString);
            } catch (JsonProcessingException e) {
                LOGGER.error(e, e);
            }
            try {
                final List<String> cEdges = new ArrayList<>();
                for (final PopulationLink edge : experiment.getExperiment().getPopulation().getEdges()) {
                    if (edge.getState().equals("1")) {
                        cEdges.add(edge.getDefaultEdge().toString());
                    }
                }
                final String edgesString = new ObjectMapper().writeValueAsString(cEdges);
                condition.setEdges(edgesString);
            } catch (JsonProcessingException e) {
                LOGGER.error(e, e);
            }
            terminationConditionRepository.save(condition);
        } catch (Exception e) {
            LOGGER.error(e, e);
        }
        experiment.setStored(true);
        sendWs(experiment);
    }


    private Experiment storeSQL(RunnableExperiment experiment) {

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
        return experimentSql;
    }

    private TerminationStat addStat(final Experiment experiment, final String name, final String data) {
        TerminationStat interactionsStat = new TerminationStat();
        interactionsStat.setName(name);
        interactionsStat.setData(data);
        interactionsStat.setId(experiment.getId());
        return interactionsStat;
    }

    private void sendWs(final RunnableExperiment experiment) {
        final ExperimentResult res = new ExperimentResult();

        res.setId(String.valueOf(experiment.getIndex()));
        res.setSize(String.valueOf(experiment.getExperiment().getPopulationSize()));
        res.setEffectiveInteractions(String.valueOf(experiment.getExperiment().getEffectiveInteractions()));
        res.setInteractions(String.valueOf(experiment.getExperiment().getInteractions()));

        this.messagingTemplate.convertAndSend("/topic/experiments", res);
    }

    public RunnableExperiment getExperiment(long experimentId) {
        LOGGER.info(experimentMap.keySet());
        return experimentMap.get(experimentId);
    }

    public Collection<? extends RunnableExperiment> getExperiments() {
        return experimentMap.values();
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

