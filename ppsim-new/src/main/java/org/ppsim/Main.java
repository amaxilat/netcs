package org.ppsim;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.ppsim.config.ConfigFile;
import org.ppsim.config.ConfigurationParser;
import org.ppsim.config.Transition;
import org.ppsim.model.AbstractExperiment;
import org.ppsim.model.AbstractProtocol;
import org.ppsim.model.StateTriple;
import org.ppsim.model.population.PopulationLink;
import org.ppsim.model.population.PopulationNode;
import org.ppsim.scheduler.AbstractScheduler;
import org.ppsim.scheduler.RandomScheduler;

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
    protected static final Logger LOGGER = Logger.getLogger(Main.class);

    public void runExperiment(final String configFileName, final String outputFile) throws FileNotFoundException {
        final ConfigFile configFile = ConfigurationParser.parseConfigFile(configFileName);

        //the number of experiments to run.
        final int iterations = (int) configFile.getIterations();

        //Used for statistics.
        final long[] averageRounds = {0};
        final long[] maxRounds = {-1};
        final long[] minRounds = {-1};
        final StringBuilder[] detailedStatistics = new StringBuilder[iterations];

        final StringBuilder totalExecutionStatistics = new StringBuilder();
        totalExecutionStatistics.append("========DetailedStatistics========" + "\n");
        totalExecutionStatistics.append("Execution:Iterations").append("\n");
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
                    averageRounds[0] += experiment.getRound();
                    if (maxRounds[0] == -1) {
                        maxRounds[0] = experiment.getRound();
                    } else if (maxRounds[0] < experiment.getRound()) {
                        maxRounds[0] = experiment.getRound();
                    }
                    if (minRounds[0] == -1) {
                        minRounds[0] = experiment.getRound();
                    } else if (minRounds[0] > experiment.getRound()) {
                        minRounds[0] = experiment.getRound();
                    }
                    totalExecutionStatistics.append(finalI + ":" + experiment.getRound()).append("\n");
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

        totalExecutionStatistics.insert(0, "MinRounds:" + minRounds[0] + "\n");
        totalExecutionStatistics.insert(0, "AverageRounds:" + averageRounds[0] / iterations + "\n");
        totalExecutionStatistics.insert(0, "MaxRounds:" + maxRounds[0] + "\n");
        totalExecutionStatistics.insert(0, "========GlobalStatistics========" + "\n");
        totalExecutionStatistics.append("========DetailedInformation========").append("\n");
        for (StringBuilder detailedStatistic : detailedStatistics) {
            totalExecutionStatistics.append(detailedStatistic);
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
        PropertyConfigurator.configure("log4j.properties");

        final String inputFile = args[0];
        final String outputFile = args[1];
        new Main().runExperiment(inputFile, outputFile);
    }

    /**
     * Boolean
     * Implements the Aproximate Majority Protocol.
     */
    public class ConfigurableProtocol extends AbstractProtocol<String> {

        public ConfigurableProtocol(final ConfigFile configFile) {
            this.configFile = configFile;
            setupTransitionsMap();
        }

        /**
         * Define the entries of the transision map.
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
         * @param configFile
         * @param scheduler
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

            resultString.append("ExperimentEnded after ").append(getRound()).append(" interactions.\n");
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

