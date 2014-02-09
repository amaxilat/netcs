package ppsim.model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

import java.util.*;
import java.io.*;


/**
 * Executes a simple experiment.
 *
 * @param <State> the variable type for the state of the agent.
 * @param <Protocol> the protocol executed in the experiment.
 */
public abstract class AbstractExperiment<State, Protocol extends PopulationProtocol<State>> extends Thread {

    /**
     * Smallest experiment size.
     */
    public static final int EXPSIZE_SMALL = 10;

    /**
     * Largest experiment size.
     * Tested with 100000000
     */
    public static final int EXPSIZE_LARGE = 1000000;

    /**
     * Incremental step for experiment size.
     */
    public static final int EXPSIZE_STEP = 10;

    /**
     * Common number of repetions for a particular experiment instance.
     */
    public static final int EXPREPETITIONS = 1;

    /**
     * Dataset experiment size.
     * fet = 104
     * dsn = 25
     */
    public static final int DATASET_SIZE = 104;

    /**
     * Apache Log4J logger.
     */
    protected static final Logger LOGGER = Logger.getLogger("ppsim.experiment");

    /**
     * The size of the population.
     */
    private final int populationSize;

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
    private final PopulationProtocol<State> protocol;

    /**
     * Scheduler.
     */
    private final Scheduler<State> scheduler;

    /**
     * Default constructor.
     *
     * @param size    the number of agents.
     * @param popprot the population protocol.
     * @param sched   the scheduler.
     */
    public AbstractExperiment(final int size, final Protocol popprot, final Scheduler<State> sched) {
        LOGGER.info("Initializing AbstractExperiment<" + popprot.getClass().getSimpleName() + ","
                + sched.getClass().getSimpleName() + ","
                + size + ">");

        // Construct population
        protocol = popprot;
        populationSize = size;
        population = new Population<State>(size);

        // Initialize Population
        initPopulation();

        // Connect scheduler
        scheduler = sched;
        scheduler.connect(population, protocol);

        LOGGER.info("AbstractExperiment<" + protocol.getClass().getSimpleName() + ","
                + scheduler.getClass().getSimpleName() + ","
                + populationSize + "> initialized");
    }

    /**
     * Initialize the population.
     */
    protected abstract void initPopulation();

        /**
         * Start the execution of the experiment.
         */
        public void run() {
            LOGGER.info("AbstractExperiment<" + protocol.getClass().getSimpleName() + ","
                    + scheduler.getClass().getSimpleName() + ","
                    + populationSize + "> started.");

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

        LOGGER.warn("AbstractExperiment<" + protocol.getClass().getSimpleName() + ","
                + scheduler.getClass().getSimpleName() + ","
                + populationSize + "> completed at round " + round);
    }
     
        /*
         * Only for dataset experiment
         */
        public void runForDataset() {
            try {
                LOGGER.info("AbstractExperiment<" + protocol.getClass().getSimpleName() + "," + scheduler.getClass().getSimpleName() + "," + populationSize + "> started.");
                // Only for dataset experiments
                //scheduler.fileToList();
                List<Integer> l1 = new ArrayList<Integer>();
                List<Integer> l2 = new ArrayList<Integer>();
                Scanner s = new Scanner(new FileReader("C:/dataset.txt"));
                while (s.hasNext()) {
                    l1.add(s.nextInt());
                    l2.add(s.nextInt());
                }
                s.close();
                while (true) {
                    try {
                        // Invoke scheduler to conduct next interaction
                        //scheduler.interact();
                        // Only for dataset experiments
                        scheduler.interact(l1, l2, round);
                        // produce debug information
                        if (LOGGER.getLevel() == Level.DEBUG) {
                            debugRound();
                        }
                        if (checkStability()) {
                            break;
                        }
                        round++;
                    } catch (Exception ex) {
                        LOGGER.error("Exception occured", ex);
                    }
                }
                // Finalize experiment
                completeExperiment();
                LOGGER.warn("AbstractExperiment<" + protocol.getClass().getSimpleName() + "," + scheduler.getClass().getSimpleName() + "," + populationSize + "> completed at round " + round);
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(AbstractExperiment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
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
            final StringBuffer strbuf = new StringBuffer();
            strbuf.append(round);
            final Set<Map.Entry<State, Long>> states = population.getStateCount();
            for (Map.Entry<State, Long> state : states) {
                strbuf.append(" [");
                strbuf.append(state.getKey());
                strbuf.append(" , ");
                strbuf.append(state.getValue());
                strbuf.append(']');
            }
            LOGGER.debug(strbuf.toString());
        }

    /**
     * The size of the population.
     *
     * @return the number of agents.
     */
    public int getPopulationSize() {
        return populationSize;
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

}
