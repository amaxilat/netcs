package org.ppsim.model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ppsim.model.population.Population;


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

    /**
     * Default constructor.
     *
     * @param size      the number of agents.
     * @param protocol  the population protocol.
     * @param scheduler the scheduler.
     */
    public AbstractExperiment(final int size, final Protocol protocol, final Scheduler<State> scheduler) {
        // Construct population
        this.protocol = protocol;
        this.population = new Population<>(size);

        // Initialize Population
        initPopulation();

        // Connect scheduler
        this.scheduler = scheduler;
        //this.scheduler.connect(this.population, this.protocol);

        reportStatus("initialized");
    }

    /**
     * Initialize the population.
     */
    protected abstract void initPopulation();

    /**
     * Start the execution of the experiment.
     */
    public void run() {
        reportStatus("started");

        while (true) {
            try {
                // Invoke scheduler to conduct next interaction
                scheduler.interact();

                // produce debug information
                if (LOGGER.getLevel() == Level.DEBUG) {
                    debugRound();
                }

                // Check if we have reached a stable state
                if (checkStability()) {
                    break;
                }

                // increase round counter
                round++;
            } catch (Exception ex) {
                LOGGER.error("Exception occured", ex);
            }
        }

        // Finalize experiment
        completeExperiment();

        reportStatus("completed at round " + round);
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
        System.out.println("AbstractExperiment<" + protocol.getClass().getSimpleName() + ","
                + scheduler.getClass().getSimpleName() + ","
                + population.size() + "> " + status);
    }

}
