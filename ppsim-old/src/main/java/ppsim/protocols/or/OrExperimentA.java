package ppsim.protocols.or;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
* Implementation of an experiment of the OR protocol.
*/
public class OrExperimentA extends AbstractExperiment<Boolean, OrProtocol> {

    /**
     * Population percentage that should be affected by OR operator before the
     * experiment is completed.
     */
    private final double populationLimit;

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     * @param limit     the percentage of population that must stabilize.
     */
    public OrExperimentA(final Scheduler<Boolean> scheduler, final int size, final double limit) {
        super(size, new OrProtocol(), scheduler);
        populationLimit = limit;
        LOGGER.debug("OrExperimentA(" + getSchedulerName() + ")<" + size + "> initialized");
    }

    /**
     * Initialize the population.
     */
    protected void initPopulation() {
        for (int i = 0; i < getPopulationSize(); i++) {
            new OrAgent(getPopulation()); //NOPMD
        }

        // Set only 1 agent with True state
        getPopulation().getAgent(getPopulationSize() - 1).setState(true);
    }

    /**
     * Evaluates if the population protocol has reached a stable state.
     *
     * @return true if the protocol has reached a stable state.
     */
    protected boolean checkStability() {
        final long target = (long) (getPopulationSize() * populationLimit);
        return getPopulation().getStateCount(true) >= target;
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        LOGGER.warn("OrExperimentA(" + getSchedulerName() + ")<" + getPopulationSize() + "," + populationLimit
                + "> completed at round " + getRound());
    }

    /**
     * Main function.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger("ppsim.protocols.or");

        long[][] stats = new long[(int) java.lang.Math.log10(EXPSIZE_LARGE) + 1][SchedulerFactory.TOTAL];
        final SchedulerFactory<Boolean> sfactory = new SchedulerFactory<Boolean>();

        for (int size = EXPSIZE_SMALL; size < EXPSIZE_LARGE; size *= EXPSIZE_STEP) {
        {
            //final int size = 10;
            //for (int sched = 7; sched < 15; sched++) {
            {
                final int sched = SchedulerFactory.RANDOM;

                    try {
                        // Instantiate the new scheduler
                        final Scheduler<Boolean> scheduler = sfactory.createScheduler(sched);

                        // Construct new ppsim.model.AbstractExperiment
                        final OrExperimentA exp = new OrExperimentA(scheduler, size, 0.5);

                        // Start the experiment
                        exp.run();

                        // Keep track of rounds
                        stats[(int) java.lang.Math.log10(size)][sched] += exp.getRound();

                    } catch (Exception ex) {
                        logger.error("Error occured", ex);
                    }

                // Average out repetitions
                stats[(int) java.lang.Math.log10(size)][sched] /= EXPREPETITIONS;
            }

            final StringBuffer strBuf = new StringBuffer(60);
            strBuf.append('\n');
            strBuf.append("prot");
            strBuf.append('\t');
            strBuf.append("size");
            strBuf.append('\t');
            strBuf.append("sched");
            strBuf.append('\t');
            strBuf.append("interactions");
            strBuf.append('\n');

            for (int thisSize = EXPSIZE_SMALL; thisSize < EXPSIZE_LARGE; thisSize *= EXPSIZE_STEP) {
            {
                //final int thisSize = 1000;
                for (int sched = 0; sched < 1; sched++) {
                    strBuf.append("or");
                    strBuf.append('\t');
                    strBuf.append(thisSize);
                    strBuf.append('\t');
                    strBuf.append(sched);
                    strBuf.append('\t');
                    strBuf.append(stats[(int) java.lang.Math.log10(thisSize)][sched]);
                    strBuf.append('\n');
                }
            }
            }

            logger.info(strBuf.toString());
        }
        }
    }


}
