package org.netcs.model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.netcs.config.ConfigFile;
import org.netcs.model.population.Population;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;
import org.netcs.scheduler.AbstractScheduler;

import java.util.Iterator;


/**
 * Executes a simple experiment.
 *
 * @param <State>    the variable type for the state of the agent.
 * @param <Protocol> the protocol executed in the experiment.
 */
public abstract class AbstractExperiment<State, Protocol extends AbstractProtocol<State>> extends Thread {

    /**
     * Apache Log4J logger.
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractExperiment.class);

    /**
     * Current round of experiment.
     */
    private long round = 0;

    /**
     * Population holder.
     */
    private final Population<State> population;

    /**
     * Actual protocol.
     */
    private final AbstractProtocol<State> protocol;

    /**
     * Scheduler.
     */
    private final Scheduler<State> scheduler;
    public ConfigFile configFile;
    public String resultString;

    /**
     * Default constructor.
     *
     * @param protocol  the population protocol.
     * @param scheduler the scheduler.
     */
    public AbstractExperiment(final ConfigFile configFile, final AbstractProtocol<State> protocol, final AbstractScheduler<State> scheduler) {
        // Construct population
        this.protocol = protocol;
        this.population = new Population<>(configFile.getPopulationSize());
        this.configFile = configFile;
        // Initialize Population
        initPopulation();

        scheduler.connect(population, protocol);
        // Connect scheduler
        this.scheduler = scheduler;
        //this.scheduler.connect(this.population, this.protocol);

        //reportStatus("initialized");
    }

    /**
     * Initialize the population.
     */
    public void initPopulation() {
        Iterator<PopulationNode<State>> nodeIterator = getPopulation().getNodes().iterator();
        while (nodeIterator.hasNext()) {
            PopulationNode node = nodeIterator.next();
            node.setState(configFile.getInitialNodeState());
            LOGGER.debug(node);
        }
        Iterator<PopulationLink<State>> edgeIterator = getPopulation().getEdges().iterator();
        while (edgeIterator.hasNext()) {
            PopulationLink edge = edgeIterator.next();
            edge.setState(configFile.getInitialLinkState());
            LOGGER.debug(edge);
        }
    }

    ;

    /**
     * Start the execution of the experiment.
     */
    public void run() {
        //reportStatus("started");

        while (true) {
            try {
                LOGGER.debug("interact " + round);
                // Invoke scheduler to conduct next interaction
                boolean interactionStatus = scheduler.interact();

                if (interactionStatus) {
                    // produce debug information
                    if (LOGGER.getLevel() == Level.DEBUG) {
                        debugRound();
                    }

                    // Check if we have reached a stable state
                    if (checkStability()) {
                        break;
                    }
                }

                // increase round counter
                round++;
                if (round % 1000000 == 0) {
//                    reportStatus("Round: " + round);
                }
            } catch (Exception ex) {
                LOGGER.error("Exception occured", ex);
            }
        }

        // Finalize experiment
        completeExperiment();

        reportStatus("Experiment Completed after " + round + " rounds.");
    }

    /**
     * Evaluates if the population protocol has reached a stable state.
     *
     * @return true if the protocol has reached a stable state.
     */
    protected abstract boolean checkStability();


    /**
     * Finalizes the execution of the experiment.
     */
    protected abstract void completeExperiment();

    /**
     * Prepares a debug output with the total number of agents per protocol state.
     */
    protected void debugRound() {
//        final StringBuffer strbuf = new StringBuffer();
//        strbuf.append(round);
//        final Set<Map.Entry<State, Long>> states = population.getStateCount();
//        for (Map.Entry<State, Long> state : states) {
//            strbuf.append(" [");
//            strbuf.append(state.getKey());
//            strbuf.append(" , ");
//            strbuf.append(state.getValue());
//            strbuf.append(']');
//        }
//        LOGGER.debug(strbuf.toString());
    }

    /**
     * The size of the population.
     *
     * @return the number of agents.
     */
    public int getPopulationSize() {
        return population.size();
    }

    /**
     * The round of execution.
     *
     * @return the number of rounds executed.
     */
    public long getRound() {
        return round;
    }

    /**
     * The population protocol holder.
     *
     * @return the population holder.
     */
    public Population<State> getPopulation() {
        return population;
    }

    /**
     * Access the name of the scheduler.
     *
     * @return the class name of the scheduler.
     */
    protected final String getSchedulerName() {
        return scheduler.getClass().getSimpleName();
    }

    private void reportStatus(final String status) {
        LOGGER.info(status);
    }

    public String getResultString() {
        return resultString;
    }
}
