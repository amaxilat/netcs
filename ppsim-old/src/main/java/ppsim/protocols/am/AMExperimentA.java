package ppsim.protocols.am;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
 * Implementation of an experiment of the Leader-Follower protocol.
 */
public class AMExperimentA extends AbstractExperiment<AMState, AMProtocol> {

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
    public AMExperimentA(final Scheduler<AMState> scheduler, final int size, final double popX, final double popY) {
        super(size, new AMProtocol(), scheduler);
        populationX = (int) (size * popX);
        populationY = (int) (size * popY);

        for (int i = 0; i < populationX; i++) {
            getPopulation().getAgent(i).setState(AMState.STATE_X);
        }

        for (int i = size - populationY; i < size; i++) {
            getPopulation().getAgent(i).setState(AMState.STATE_Y);
        }

        LOGGER.debug("AMExperimentA(" + getSchedulerName() + ")<" + size + ","
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
        final long popLimit = (long) (0.8 * getPopulationSize());

        LOGGER.debug("X=" + popX + ", y=" + popY + ", b=" + popB);
        return (popX >= popLimit)
                ||
                (popY >= popLimit);
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        final long popX = getPopulation().getStateCount(AMState.STATE_X);
        final long popLimit = (long) (0.8 * getPopulationSize());

        if (popX >= popLimit) {
            LOGGER.warn("AMExperimentA(" + getSchedulerName() + ")<" + getPopulationSize() + ","
                    + populationX + ","
                    + populationY + "> completed at round " + getRound() + " -- more X");
        } else {
            LOGGER.warn("AMExperimentA(" + getSchedulerName() + ")<" + getPopulationSize() + ","
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

        //final double[] limitX = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        //final double[] limitY = {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};

        final double[] limitX = {0.6, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        final double[] limitY = {0.4, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};

        long[][][] stats = new long[(int) java.lang.Math.log10(EXPSIZE_LARGE) + 1][SchedulerFactory.TOTAL][limitX.length];

        final SchedulerFactory<AMState> sfactory = new SchedulerFactory<AMState>();

        //for (int aLimit = 0; aLimit < limitX.length; aLimit++) {
        final int aLimit = 0;


        for (int size = EXPSIZE_SMALL; size < EXPSIZE_LARGE; size *= EXPSIZE_STEP) {


            //for (int sched = SchedulerFactory.LOLLIPOP_EDGE;sched < SchedulerFactory.LOLLIPOP_STATE; sched++) {
            final int sched = SchedulerFactory.BINARY_TREE_EDGE;
            for (int repetition = 0; repetition < EXPREPETITIONS; repetition++) {
                try {
                    // Instantiate the new scheduler
                    final Scheduler<AMState> scheduler = sfactory.createScheduler(sched);

                    // Construct new ppsim.model.AbstractExperiment
                    final AMExperimentA exp = new AMExperimentA(scheduler, size, limitX[aLimit], limitY[aLimit]);

                    // Start the experiment
                    exp.run();

                    // Keep track of rounds
                    stats[(int) java.lang.Math.log10(size)][sched][aLimit] += exp.getRound();

                } catch (Exception ex) {
                    logger.error("Exception occured", ex);
                }
            }

            // Average out repetitions
            stats[(int) java.lang.Math.log10(size)][sched][aLimit] /= EXPREPETITIONS;
            //}

            final StringBuffer strBuf = new StringBuffer(60);
            strBuf.append('\n');
            strBuf.append("prot");
            strBuf.append('\t');
            strBuf.append("size");
            strBuf.append('\t');
            strBuf.append("sched");
            strBuf.append('\t');
            for (double aLimitX : limitX) {
                strBuf.append(aLimitX);
                strBuf.append('\t');
            }
            strBuf.append('\n');

            for (int thisSize = EXPSIZE_SMALL; thisSize < EXPSIZE_LARGE; thisSize *= EXPSIZE_STEP) {
                for (int schedul = 19; schedul < 20; schedul++) {
                    strBuf.append("am");
                    strBuf.append('\t');
                    strBuf.append(thisSize);
                    strBuf.append('\t');
                    strBuf.append(sched);
                    strBuf.append('\t');
                    for (int thisLimit = 0; thisLimit < limitX.length; thisLimit++) {
                        strBuf.append(stats[(int) java.lang.Math.log10(thisSize)][sched][thisLimit]);
                        strBuf.append('\t');
                    }
                    strBuf.append('\n');
                }
            }

            logger.info(strBuf.toString());
        }

    }
}
