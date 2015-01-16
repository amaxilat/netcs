package org.netcs;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;
import org.netcs.config.ConfigFile;
import org.netcs.model.ConfigurableExperiment;
import org.netcs.model.population.PopulationNode;
import org.netcs.scheduler.RandomScheduler;
import org.springframework.messaging.core.MessageSendingOperations;

/**
 * Main class that executes the experiment.
 */
public class AdvancedRunnableExperiment extends RunnableExperiment {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(AdvancedRunnableExperiment.class);

    public AdvancedRunnableExperiment(final String algorithmName, final ConfigFile configFile, final Long nodeCount, final long index, final MessageSendingOperations<String> messagingTemplate) {
        super(algorithmName, configFile, nodeCount, index,messagingTemplate);
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

            //prepare experiment
            final ConfigurableProtocol protocol = new ConfigurableProtocol(configFile);
            final RandomScheduler<String> scheduler = new RandomScheduler<>();
            LOGGER.info("experiment:" + index);
            experiment = new ConfigurableExperiment(configFile, protocol, scheduler, index);
            LOGGER.info("willinit:" + index);
            experiment.initPopulation();
            PopulationNode node = (PopulationNode) experiment.getPopulation().getNodes().iterator().next();
            LOGGER.info("Set Leader: " + node);
            node.setState("l");

            LOGGER.info("initdone:" + index);
            //run experiment
            experiment.run();

            //get statistics
            summaryStatistics.addValue(experiment.getInteractions());
            effectiveSummaryStatistics.addValue(experiment.getEffectiveInteractions());
            if (configFile.getDebugLevel() > 1) {
                totalExecutionStatistics.append(index).append(":").append(experiment.getInteractions()).append(":").append(experiment.getEffectiveInteractions()).append("\n");
            }
            detailedStatistics.append("Iteration:").append(index).append("\n");
            detailedStatistics.append(experiment.getResultString()).append("\n");

            totalExecutionStatistics.insert(0, "MinInteractions:" + summaryStatistics.getMin() + ":" + effectiveSummaryStatistics.getMin() + "\n");
            totalExecutionStatistics.insert(0, "AverageInteractions:" + summaryStatistics.getMean() + ":" + effectiveSummaryStatistics.getMean() + "\n");
            totalExecutionStatistics.insert(0, "MaxInteractions:" + summaryStatistics.getMax() + ":" + effectiveSummaryStatistics.getMax() + "\n");
            totalExecutionStatistics.insert(0, "Variance:" + summaryStatistics.getVariance() + ":" + effectiveSummaryStatistics.getVariance() + "\n");
            totalExecutionStatistics.insert(0, "StandardDeviation:" + summaryStatistics.getStandardDeviation() + ":" + effectiveSummaryStatistics.getStandardDeviation() + "\n");
            totalExecutionStatistics.insert(0, "#========GlobalStatistics========" + "\n");
            if (configFile.getDebugLevel() > 2) {
                totalExecutionStatistics.append("#========DetailedInformation========").append("\n");
//            for (StringBuilder detailedStatistic : detailedStatistics) {
//                totalExecutionStatistics.append(detailedStatistic);
//            }
            }

            fileWriter.write(totalExecutionStatistics.toString());
            fileWriter.close();

            LOGGER.info("Experiment ended");
        } catch (Exception e) {
            LOGGER.error(e, e);
        }
    }
}

