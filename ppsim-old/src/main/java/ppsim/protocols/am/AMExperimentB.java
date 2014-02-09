package ppsim.protocols.am;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
* Implementation of an experiment of the Leader-Follower protocol.
*/
public class AMExperimentB extends AbstractExperiment<AMState, AMProtocol> {

    /**
     * Number of rounds to produce statistics.
     */
    public static final int TOTROUNDS_500 = 3000;

    /**
     * Local variable for storing statistics.
     */
    private long[][] expStats = new long[3][TOTROUNDS_500];

    /**
     * Total number of agents at state X in this experiment.
     */
    private final int populationX;

    /**
     * Total number of agents at state Y in this experiment.
     */
    private final int populationY;

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     * @param popX      the percentage of population that are at state X
     * @param popY      the percentage of population that are at state Y
     */
    public AMExperimentB(final Scheduler<AMState> scheduler, final int size, final double popX, final double popY) {
        super(size, new AMProtocol(), scheduler);
        populationX = (int) (size * popX);
        populationY = (int) (size * popY);

        for (int i = 0; i < populationX; i++) {
            getPopulation().getAgent(i).setState(AMState.STATE_X);
        }

        for (int i = size - populationY; i < size; i++) {
            getPopulation().getAgent(i).setState(AMState.STATE_Y);
        }

        LOGGER.debug("AMExperimentB(" + getSchedulerName() + ")<" + size + ","
                + populationX + ","
                + populationY + "> initialized");
    }

    /**
     * Initialize the population.
     */
    protected void initPopulation() {
        for (long i = 0; i < getPopulationSize(); i++) {
            new AMAgent(getPopulation()); //NOPMD
        }
    }

    /**
     * Evaluates if the population protocol has reached a stable state.
     *
     * @return true if the protocol has reached a stable state.
     */
    protected boolean checkStability() {
        final long popX = getPopulation().getStateCount(AMState.STATE_X);
        final long popY = getPopulation().getStateCount(AMState.STATE_Y);
        final long popB = getPopulation().getStateCount(AMState.STATE_B);

        // Keep track of rounds
        final long thisRound = getRound() % 500;
        if (thisRound == 0) {
            final int thisSlot = (int) getRound() / 500;
            expStats[0][thisSlot] = popX;
            expStats[1][thisSlot] = popB;
            expStats[2][thisSlot] = popY;
        }

        LOGGER.debug("x=" + popX + ", y=" + popY + ", popB=" + popB);
        return (popX >= getPopulationSize())
                ||
                (popY >= getPopulationSize());
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        final long popX = getPopulation().getStateCount(AMState.STATE_X);

        if (popX >= getPopulationSize()) {
            LOGGER.warn("AMExperimentB(" + getSchedulerName() + ")<" + getPopulationSize() + ","
                    + populationX + ","
                    + populationY + "> completed at round " + getRound() + " -- more X");
        } else {
            LOGGER.warn("AMExperimentB(" + getSchedulerName() + ")<" + getPopulationSize() + ","
                    + populationX + ","
                    + populationY + "> completed at round " + getRound() + " -- more Y");
        }
    }

    /**
     * Main function.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger("ppsim.protocols.am");
        long[][] stats = new long[3][TOTROUNDS_500];

        final int size = 10000;
        final int sched = SchedulerFactory.RANDOM;
        final SchedulerFactory<AMState> sfactory = new SchedulerFactory<AMState>();
        for (int repetition = 0; repetition < EXPREPETITIONS; repetition++) {
            try {
                // Instantiate the new scheduler
                final Scheduler<AMState> scheduler = sfactory.createScheduler(sched);

                // Construct new ppsim.model.AbstractExperiment
                final AMExperimentB exp = new AMExperimentB(scheduler, size, 0.15, 0.1);

                // Start the experiment
                exp.run();

                // Copy statistics
                for (int x = 0; x < TOTROUNDS_500; x++) {
                    for (int s = 0; s < 3; s++) {
                        stats[s][x] += exp.expStats[s][x];
                    }
                }

                // Copy last value to remaining slots
                final int finalSlot = (int) exp.getRound() / TOTROUNDS_500;
                for (int x = finalSlot + 1; x < TOTROUNDS_500; x++) {
                    stats[0][x] += size;
                }

            } catch (Exception ex) {
                logger.error("Exception occured", ex);
            }
        }

        // Average out repetitions
        for (int x = 0; x < TOTROUNDS_500; x++) {
            for (int s = 0; s < 3; s++) {
                stats[s][x] /= EXPREPETITIONS;
            }
        }

        final StringBuffer strBuf = new StringBuffer(60);
        strBuf.append('\n');
        strBuf.append("prot");
        strBuf.append('\t');
        strBuf.append("size");
        strBuf.append('\t');
        strBuf.append("sched");
        strBuf.append('\t');
        strBuf.append("round");
        strBuf.append('\t');
        strBuf.append("x");
        strBuf.append('\t');
        strBuf.append('b');
        strBuf.append('\t');
        strBuf.append('y');
        strBuf.append('\n');

        // Print statistics
        for (int x = 0; x < TOTROUNDS_500; x++) {
            strBuf.append("am");
            strBuf.append('\t');
            strBuf.append(size);
            strBuf.append('\t');
            strBuf.append(sched);
            strBuf.append('\t');
            strBuf.append(x * TOTROUNDS_500);
            for (int s = 0; s < 3; s++) {
                strBuf.append('\t');
                strBuf.append(stats[s][x]);
            }
            strBuf.append('\n');
        }

        logger.info(strBuf.toString());
    }
}
