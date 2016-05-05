package org.netcs;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;
import org.netcs.config.ConfigFile;
import org.netcs.config.Transition;
import org.netcs.model.AbstractExperiment;
import org.netcs.model.AbstractProtocol;
import org.netcs.model.ConfigurableExperiment;
import org.netcs.model.StateTriple;
import org.netcs.scheduler.*;
import org.netcs.service.LookupService;
import org.springframework.messaging.core.MessageSendingOperations;

import java.io.FileWriter;

/**
 * Main class that executes the experiment.
 */
public class RunnableExperiment implements Runnable {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(RunnableExperiment.class);
    protected final long index;
    protected final Long nodeCount;
    protected final ConfigFile configFile;
    private final String algorithmName;
    private final MessageSendingOperations<String> messagingTemplate;
    private final AbstractScheduler scheduler;
    protected StringBuilder detailedStatistics;
    protected AbstractExperiment experiment;
    protected boolean stored;
    protected FileWriter fileWriter;
    protected final LookupService lookupService;

    public AbstractExperiment getExperiment() {
        return experiment;
    }

    public RunnableExperiment(final String algorithmName, final ConfigFile configFile, final Long nodeCount, final long index, final MessageSendingOperations<String> messagingTemplate
            , final LookupService lookupService) {
        this(algorithmName, "Random", configFile, nodeCount, index, messagingTemplate, lookupService);
    }

    public RunnableExperiment(final String algorithmName, final String schedulerName, final ConfigFile configFile, final Long nodeCount, final long index, final MessageSendingOperations<String> messagingTemplate
            , final LookupService lookupService) {
        this.lookupService = lookupService;
        this.messagingTemplate = messagingTemplate;
        this.algorithmName = algorithmName;
        this.configFile = configFile;
        LOGGER.info("NodeCount:" + nodeCount);
        configFile.setPopulationSize(nodeCount);
        this.nodeCount = nodeCount;
        this.index = index;
        this.stored = false;

        //prepare experiment
        final ConfigurableProtocol protocol = new ConfigurableProtocol(configFile);
//        final Random scheduler = new Random();
        if ("Random".equals(schedulerName)) {
            scheduler = new Random();
        } else if ("History".equals(schedulerName)) {
            scheduler = new History();
        } else if ("ReverseHistory".equals(schedulerName)) {
            scheduler = new ReverseHistory();
        } else if ("Connection".equals(schedulerName)) {
            scheduler = new Connection();
        } else if ("PerfectMatching".equals(schedulerName)) {
            scheduler = new PerfectMatching();
        } else {
            scheduler = new Random();
        }
        LOGGER.info("experiment:" + index);
        experiment = new ConfigurableExperiment(configFile, protocol, scheduler, index, lookupService);

    }

    public AbstractScheduler getScheduler() {
        return scheduler;
    }

    public long getIndex() {
        return index;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public Long getNodeCount() {
        return nodeCount;
    }

    public StringBuilder getDetailedStatistics() {
        return detailedStatistics;
    }

    public boolean isFinished() {
        return experiment.isFinished();
    }

    @Override
    public void run() {
        try {

            if (nodeCount != null) {
                configFile.setPopulationSize(nodeCount);
            }

            //Used for statistics.
            final SummaryStatistics summaryStatistics = new SummaryStatistics();
            final SummaryStatistics effectiveSummaryStatistics = new SummaryStatistics();

            detailedStatistics = new StringBuilder();

            final StringBuilder totalExecutionStatistics = new StringBuilder();
            if (configFile.getDebugLevel() > 1) {
                totalExecutionStatistics.append("#========DetailedStatistics========" + "\n");
                totalExecutionStatistics.append("Execution:Iterations").append("\n");
            }

            LOGGER.info("calling initPopulation");
            experiment.initPopulation();


            //run experiment
            experiment.run();
            //get statistics
            LOGGER.info("ExperimentDone");
            summaryStatistics.addValue(experiment.getInteractions());
            effectiveSummaryStatistics.addValue(experiment.getEffectiveInteractions());
//            if (configFile.getDebugLevel() > 1) {
//                totalExecutionStatistics.append(index).append(":").append(experiment.getInteractions()).append(":").append(experiment.getEffectiveInteractions()).append("\n");
//            }
//            detailedStatistics.append("Iteration:").append(index).append("\n");
//            detailedStatistics.append(experiment.getResultString()).append("\n");
//
//            totalExecutionStatistics.insert(0, "MinInteractions:" + summaryStatistics.getMin() + ":" + effectiveSummaryStatistics.getMin() + "\n");
//            totalExecutionStatistics.insert(0, "AverageInteractions:" + summaryStatistics.getMean() + ":" + effectiveSummaryStatistics.getMean() + "\n");
//            totalExecutionStatistics.insert(0, "MaxInteractions:" + summaryStatistics.getMax() + ":" + effectiveSummaryStatistics.getMax() + "\n");
//            totalExecutionStatistics.insert(0, "Variance:" + summaryStatistics.getVariance() + ":" + effectiveSummaryStatistics.getVariance() + "\n");
//            totalExecutionStatistics.insert(0, "StandardDeviation:" + summaryStatistics.getStandardDeviation() + ":" + effectiveSummaryStatistics.getStandardDeviation() + "\n");
//            totalExecutionStatistics.insert(0, "#========GlobalStatistics========" + "\n");
//            if (configFile.getDebugLevel() > 2) {
//                totalExecutionStatistics.append("#========DetailedInformation========").append("\n");
////            for (StringBuilder detailedStatistic : detailedStatistics) {
////                totalExecutionStatistics.append(detailedStatistic);
////            }
//            }
//
//            fileWriter.write(totalExecutionStatistics.toString());
//            fileWriter.close();

            LOGGER.info("Experiment ended:" + index);
        } catch (Exception e) {
            LOGGER.error(e, e);
        }
    }

    public boolean isStored() {
        return stored;
    }

    public void setStored(boolean stored) {
        this.stored = stored;
    }

    public void setFileWriter(final FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public class ConfigurableProtocol extends AbstractProtocol {

        public ConfigurableProtocol(final ConfigFile configFile) {
            this.configFile = configFile;
            setupTransitionsMap();
        }

        /**
         * Define the entries of the transition map.
         */
        protected void setupTransitionsMap() {
            for (Transition tr : configFile.getTransitions()) {
                this.addEntry(
                        new StateTriple(tr.getNode1(), tr.getNode2(), tr.getLink()),
                        new StateTriple(tr.getNode1New(), tr.getNode2New(), tr.getLinkNew()));
            }
        }
    }
}

