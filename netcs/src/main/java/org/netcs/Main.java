package org.netcs;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
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

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class that executes the experiment.
 */
public class Main {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    void runExperiment(final String configFileName, final String outputFile, final Long nodeCount) throws FileNotFoundException {
        final ConfigFile configFile = ConfigurationParser.parseConfigFile(configFileName);

        if (nodeCount != null) {
            configFile.setPopulationSize(nodeCount);
        }
        //the number of experiments to run.
        final int iterations = (int) configFile.getIterations();

        //Used for statistics.
        final SummaryStatistics summaryStatistics = new SummaryStatistics();
        final SummaryStatistics effectiveSummaryStatistics = new SummaryStatistics();

        final StringBuilder[] detailedStatistics = new StringBuilder[iterations];

        final StringBuilder totalExecutionStatistics = new StringBuilder();
        if (configFile.getDebugLevel() > 1) {
            totalExecutionStatistics.append("#========DetailedStatistics========" + "\n");
            totalExecutionStatistics.append("Execution:Iterations").append("\n");
        }
        final CountDownLatch latch = new CountDownLatch(iterations);

        ExecutorService executor = Executors.newFixedThreadPool(5);


        for (int i = 0; i < iterations; i++) {
            final int finalI = i;
            executor.submit(new Runnable() {
                @Override
                public void run() {

                    detailedStatistics[finalI] = new StringBuilder();

                    //prepare experiment
                    final ConfigurableProtocol protocol = new ConfigurableProtocol(configFile);
                    final RandomScheduler<String> scheduler = new RandomScheduler<>();
                    final ConfigurableExperiment experiment = new ConfigurableExperiment(configFile, protocol, scheduler);
                    experiment.initPopulation();

                    //run experiment
                    experiment.run();

                    //get statistics
                    summaryStatistics.addValue(experiment.getInteractions());
                    effectiveSummaryStatistics.addValue(experiment.getEffectiveInteractions());
                    if (configFile.getDebugLevel() > 1) {
                        totalExecutionStatistics.append(finalI).append(":").append(experiment.getInteractions()).append(":").append(experiment.getEffectiveInteractions()).append("\n");
                    }
                    detailedStatistics[finalI].append("Iteration:").append(finalI).append("\n");
                    detailedStatistics[finalI].append(experiment.getResultString()).append("\n");
                    latch.countDown();
                }
            });
        }
        while (latch.getCount() > 0) {
            try {
                Thread.sleep(10000);
                LOGGER.info(latch.getCount() + " experiments still running...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        totalExecutionStatistics.insert(0, "MinInteractions:" + summaryStatistics.getMin() + ":" + effectiveSummaryStatistics.getMin() + "\n");
        totalExecutionStatistics.insert(0, "AverageInteractions:" + summaryStatistics.getMean() + ":" + effectiveSummaryStatistics.getMean() + "\n");
        totalExecutionStatistics.insert(0, "MaxInteractions:" + summaryStatistics.getMax() + ":" + effectiveSummaryStatistics.getMax() + "\n");
        totalExecutionStatistics.insert(0, "Variance:" + summaryStatistics.getVariance() + ":" + effectiveSummaryStatistics.getVariance() + "\n");
        totalExecutionStatistics.insert(0, "StandardDeviation:" + summaryStatistics.getStandardDeviation() + ":" + effectiveSummaryStatistics.getStandardDeviation() + "\n");
        totalExecutionStatistics.insert(0, "#========GlobalStatistics========" + "\n");
        if (configFile.getDebugLevel() > 2) {
            totalExecutionStatistics.append("#========DetailedInformation========").append("\n");
            for (StringBuilder detailedStatistic : detailedStatistics) {
                totalExecutionStatistics.append(detailedStatistic);
            }
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
        executor.shutdown();
    }

    public static void main(String[] args) throws FileNotFoundException {
        PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResourceAsStream("log4j.properties"));

        final String inputFile = args[0];
        final String outputFile = args[1];
        Long nodeCount = null;
        if (args.length > 2) {
            nodeCount = Long.valueOf(args[2]);
        }
        new Main().runExperiment(inputFile, outputFile, nodeCount);
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


    class ConfigurableExperiment extends AbstractExperiment<String, AbstractProtocol<String>> {

        /**
         * Default constructor.
         *
         * @param configFile the configuration file to use.
         * @param scheduler  the scheduler to use.
         */
        public ConfigurableExperiment(ConfigFile configFile, final AbstractProtocol<String> protocol, AbstractScheduler<String> scheduler) {
            super(configFile, protocol, scheduler);
        }

        @Override
        protected boolean checkStability() {
            Boolean result = false;
            int edgeCount = 0;

            for (PopulationLink<String> edge : getPopulation().getEdges()) {
                if (edge.getState().equals("1")) {
                    edgeCount++;
                    if (edgeCount > getPopulationSize()) return false;
                }
            }
            //TODO: check for more terminating conditions
            if (edgeCount == getPopulationSize() - 1) {
                LOGGER.info("Detected Terminating Condition: Line");
                return true;
            }
            return result;
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

