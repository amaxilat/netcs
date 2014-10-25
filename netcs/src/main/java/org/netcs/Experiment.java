package org.netcs;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;
import org.netcs.config.ConfigFile;
import org.netcs.config.ConfigurationParser;
import org.netcs.config.Transition;
import org.netcs.model.AbstractExperiment;
import org.netcs.model.AbstractProtocol;
import org.netcs.model.StateTriple;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;
import org.netcs.scheduler.AbstractScheduler;
import org.netcs.scheduler.RandomScheduler;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Main class that executes the experiment.
 */
public class Experiment implements Runnable {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(Experiment.class);
    private final long index;
    private final String configFileName;
    private final String outputFile;
    private final Long nodeCount;
    private final String algorithmName;
    private StringBuilder detailedStatistics;
    private ConfigurableExperiment experiment;
    private boolean finished;
    private boolean lookingForStar;
    private boolean lookingForCircle;
    private boolean lookingForCycleCover;
    private boolean lookingForLine;
    private boolean stored;

    public ConfigurableExperiment getExperiment() {
        return experiment;
    }

    public Experiment(String configFileName, String outputFile, Long nodeCount, final long index) {
        this.algorithmName = configFileName.replaceAll(".prop", "");
        this.configFileName = configFileName;
        this.outputFile = outputFile;
        this.nodeCount = nodeCount;
        this.index = index;
        this.finished = false;
        this.lookingForStar = false;
        this.lookingForCircle = false;
        this.lookingForCycleCover = false;
        this.lookingForLine = false;
        this.stored = false;

    }

    public long getIndex() {
        return index;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public Long getNodeCount() {
        return nodeCount;
    }

    public StringBuilder getDetailedStatistics() {
        return detailedStatistics;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isLookingForStar() {
        return lookingForStar;
    }

    public void setLookingForStar(boolean lookingForStar) {
        this.lookingForStar = lookingForStar;
    }

    public boolean isLookingForCircle() {
        return lookingForCircle;
    }

    public void setLookingForCircle(boolean lookingForCircle) {
        this.lookingForCircle = lookingForCircle;
    }

    public boolean isLookingForCycleCover() {
        return lookingForCycleCover;
    }

    public void setLookingForCycleCover(boolean lookingForCycleCover) {
        this.lookingForCycleCover = lookingForCycleCover;
    }

    public boolean isLookingForLine() {
        return lookingForLine;
    }

    public void setLookingForLine(boolean lookingForLine) {
        this.lookingForLine = lookingForLine;
    }

    @Override
    public void run() {
        try {
            final ConfigFile configFile = ConfigurationParser.parseConfigFile(configFileName);

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
            experiment = new ConfigurableExperiment(configFile, protocol, scheduler,index);
            LOGGER.info("willinit:" + index);
            experiment.initPopulation();
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

            //write statistics to file
            try {
                FileWriter fw = new FileWriter(outputFile);
                fw.write(totalExecutionStatistics.toString());
                fw.close();
                LOGGER.info(totalExecutionStatistics);
            } catch (IOException e) {
                LOGGER.error(e, e);
            }

            LOGGER.info("All experiments ended Successfully!");
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

    public class ConfigurableProtocol extends AbstractProtocol<String> {

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
                        new StateTriple<>(tr.getNode1(), tr.getNode2(), tr.getLink()),
                        new StateTriple<>(tr.getNode1New(), tr.getNode2New(), tr.getLinkNew()));
            }
        }
    }


    public class ConfigurableExperiment extends AbstractExperiment<String, AbstractProtocol<String>> {

        /**
         * Default constructor.
         *
         * @param configFile the configuration file to use.
         * @param scheduler  the scheduler to use.
         * @param index
         */
        public ConfigurableExperiment(ConfigFile configFile, final AbstractProtocol<String> protocol, AbstractScheduler<String> scheduler, long index) {
            super(configFile, protocol, scheduler,index);
        }

        @Override
        protected boolean checkStability() {

            if (lookingForStar && checkStar()) {
                finished = true;
                return true;
            }
            if (lookingForCircle && checkCircle()) {
                finished = true;
                return true;
            }
            if (lookingForCycleCover && checkCycleCover()) {
                finished = true;
                return true;
            }
            if (lookingForLine && checkLine()) {
                finished = true;
                return true;
            }
            finished = false;
            return false;
        }

        @Override
        protected void completeExperiment() {
            StringBuilder resultString = new StringBuilder();

            resultString.append("ExperimentEnded after ").append(getInteractions()).append(" interactions.\n");
            resultString.append("NodeStatuses:").append("\n");
            for (PopulationNode<String> node : getPopulation().getNodes()) {
                resultString.append(node.getNodeName()).append(":").append(node.getState()).append("\n");
            }
            resultString.append("LinkStatuses:").append("\n");
            for (PopulationLink<String> edge : getPopulation().getEdges()) {
                if (edge.getState().equals("1")) {
                    resultString.append(edge.getDefaultEdge()).append(":").append(edge.getState()).append("\n");
                }
            }
            this.resultString = resultString.toString();
            LOGGER.debug(this.resultString);
        }
    }
}

