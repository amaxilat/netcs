package ppsim.protocols.lf;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
* Implementation of an experiment of the Leader-Follower protocol.
*/
public class LFExperiment extends AbstractExperiment<LFState, LFProtocol> {

    /**
     * Total number of Leaders in this experiment.
     */
    private final int populationLeaders;

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     * @param leaders   the percentage of population that are leaders
     */
    public LFExperiment(final Scheduler<LFState> scheduler, final int size, final double leaders) {
        super(size, new LFProtocol(), scheduler);
        populationLeaders = (int) (size * leaders);

        final LFState falseState = LFState.STATE_F;
        for (int i = populationLeaders; i < size; i++) {
            getPopulation().getAgent(i).setState(falseState);
        }

        LOGGER.debug("LFExperiment(" + getSchedulerName() + ")<" + size + "," + leaders + "> initialized with " + populationLeaders + " Leaders");
    }

    /**
     * Initialize the population.
     */
    protected void initPopulation() {
        for (long i = 0; i < getPopulationSize(); i++) {
            new LFAgent(getPopulation()); //NOPMD
        }
    }

    /**
     * Evaluates if the population protocol has reached a stable state.
     *
     * @return true if the protocol has reached a stable state.
     */
    protected boolean checkStability() {
        final long ones = getPopulation().getStateCount(LFState.STATE_ONE);
        final long zeros = getPopulation().getStateCount(LFState.STATE_ZERO);
        final long leaders = getPopulation().getStateCount(LFState.STATE_L);
        final long followers = getPopulation().getStateCount(LFState.STATE_F);

        LOGGER.debug("ones=" + ones + ", zeros=" + zeros + ", leaders=" + leaders + ", followers=" + followers);
        return (ones + leaders >= getPopulationSize())
                ||
                (zeros + followers >= getPopulationSize());
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        final long ones = getPopulation().getStateCount(LFState.STATE_ONE);
        final long zeros = getPopulation().getStateCount(LFState.STATE_ZERO);
        final long leaders = getPopulation().getStateCount(LFState.STATE_L);
        final long followers = getPopulation().getStateCount(LFState.STATE_F);

        if (ones + leaders > zeros + followers) {
            LOGGER.warn("LFExperiment(" + getSchedulerName() + ")<" + getPopulationSize() + "," + populationLeaders
                    + "> completed at round " + getRound() + " -- more Leaders");
        } else {
            LOGGER.warn("LFExperiment(" + getSchedulerName() + ")<" + getPopulationSize() + "," + populationLeaders
                    + "> completed at round " + getRound() + " -- more Followers");
        }
    }

    /**
     * Main function.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger("ppsim.protocols.lf");

        final double[] limit = {0.4, 0.5, 0.1, 0.2, 0.3, 0.9, 0.8, 0.7, 0.6,
                0.41, 0.42, 0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5,
                0.59, 0.58, 0.57, 0.56, 0.55, 0.54, 0.53, 0.52, 0.51};

        //final double[] limit = {0.1, 0.2, 0.3, 0.4, 0.5, 0.8, 0.9};
        //final double[] limit = {0.5};

        long[][][] stats = new long[(int) java.lang.Math.log10(EXPSIZE_LARGE) + 1][SchedulerFactory.TOTAL][limit.length];

        final SchedulerFactory<LFState> sfactory = new SchedulerFactory<LFState>();

        //double limit[] = {0.51, 0.52, 0.53, 0.54, 0.55, 0.56, 0.57, 0.58, 0.59, 0.6, 0.7, 0.8, 0.9, 1.0};
        for (int aLimit = 0; aLimit < limit.length; aLimit++) {
            for (int size = EXPSIZE_SMALL; size < EXPSIZE_LARGE; size *= EXPSIZE_STEP) {
                for (int sched = SchedulerFactory.RANDOM; sched < SchedulerFactory.GNP_NODE; sched++) {

                    for (int repetition = 0; repetition < EXPREPETITIONS; repetition++) {
                        try {
                            // Instantiate the new scheduler
                            final Scheduler<LFState> scheduler = sfactory.createScheduler(sched);

                            // Construct new ppsim.model.AbstractExperiment
                            final LFExperiment exp = new LFExperiment(scheduler, size, limit[aLimit]);

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
                }

                final StringBuffer strBuf = new StringBuffer(60);
                strBuf.append('\n');
                strBuf.append("prot");
                strBuf.append('\t');
                strBuf.append("size");
                strBuf.append('\t');
                strBuf.append("sched");
                strBuf.append('\t');
                for (int thisLimit = 0; thisLimit < limit.length; thisLimit++) {
                    strBuf.append(limit[thisLimit]);
                    strBuf.append('\t');
                }
                strBuf.append('\n');

                for (int thisSize = EXPSIZE_SMALL; thisSize < EXPSIZE_LARGE; thisSize *= EXPSIZE_STEP) {
                    for (int sched = SchedulerFactory.RANDOM; sched < SchedulerFactory.GNP_NODE; sched++) {
                        strBuf.append("lf");
                        strBuf.append('\t');
                        strBuf.append(thisSize);
                        strBuf.append('\t');
                        strBuf.append(sched);
                        strBuf.append('\t');
                        for (int thisLimit = 0; thisLimit < limit.length; thisLimit++) {
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
}
