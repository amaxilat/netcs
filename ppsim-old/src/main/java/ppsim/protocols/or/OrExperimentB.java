package ppsim.protocols.or;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
* Implementation of an experiment of the OR protocol.
*/
public class OrExperimentB extends AbstractExperiment<Boolean, OrProtocol> {

    /**
     * Rounds before producing statistics.
     */
    public static final int TOTROUNDS_500 = 3000;

    /**
     * Local variable for storing statistics.
     */
    private long[] expStats = new long[TOTROUNDS_500];

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     */
    public OrExperimentB(final Scheduler<Boolean> scheduler, final int size) {
        super(size, new OrProtocol(), scheduler);
        LOGGER.debug("OrExperimentB(" + getSchedulerName() + ")<" + size + "> initialized");
    }

    /**
     * Initialize the population.
     */
    protected void initPopulation() {
        for (int i = 0; i < getPopulationSize(); i++) {
            new OrAgent(getPopulation()); //NOPMD
        }

        // Set only 1 agent with True state
        getPopulation().getAgent(0).setState(true);
    }

    /**
     * Evaluates if the population protocol has reached a stable state.
     *
     * @return true if the protocol has reached a stable state.
     */
    protected boolean checkStability() {
        final long trueState = getPopulation().getStateCount(true);

        // Keep track of rounds
        final long thisRound = getRound() % TOTROUNDS_500;
        if (thisRound == 0) {
            final int thisSlot = (int) getRound() / TOTROUNDS_500;
            expStats[thisSlot] = trueState;
        }

        return trueState >= getPopulationSize();
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        LOGGER.warn("OrExperimentB(" + getSchedulerName() + ")<" + getPopulationSize() + "> completed at round " + getRound());
    }

    /**
     * Main function.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger("ppsim.protocols.or");
        long[][][] stats = new long[(int) java.lang.Math.log10(EXPSIZE_LARGE) + 1][SchedulerFactory.TOTAL][TOTROUNDS_500];
        final int size = 100;
        final SchedulerFactory<Boolean> sfactory = new SchedulerFactory<Boolean>();

        for (int sched = SchedulerFactory.TRANSMAP; sched < SchedulerFactory.STATE; sched++) {
            for (int repetition = 0; repetition < EXPREPETITIONS; repetition++) {
                try {
                    // Instantiate the new scheduler
                    final Scheduler<Boolean> scheduler = sfactory.createScheduler(sched);

                    // Construct new ppsim.model.AbstractExperiment
                    final OrExperimentB exp = new OrExperimentB(scheduler, size);

                    // Start the experiment
                    exp.run();

                    // Copy statistics
                    for (int x = 0; x < TOTROUNDS_500; x++) {
                        stats[(int) java.lang.Math.log10(size)][sched][x] += exp.expStats[x];
                    }

                    // Copy last value to remaining slots
                    final int finalSlot = (int) exp.getRound() / TOTROUNDS_500;
                    for (int x = finalSlot + 1; x < TOTROUNDS_500; x++) {
                        stats[(int) java.lang.Math.log10(size)][sched][x] += size;
                    }


                } catch (Exception ex) {
                    logger.error("Error occured", ex);
                }
            }

            // Average out repetitions
            for (int x = 0; x < TOTROUNDS_500; x++) {
                stats[(int) java.lang.Math.log10(size)][sched][x] /= EXPREPETITIONS;
            }

/*            final StringBuffer strBuf = new StringBuffer();
            strBuf.append('\n');
            strBuf.append("prot");
            strBuf.append('\t');
            strBuf.append("size");
            strBuf.append('\t');
            strBuf.append("sched");
            strBuf.append('\t');
            strBuf.append("round");
            strBuf.append('\t');
            strBuf.append("agents");
            strBuf.append('\n');

            // Print statistics
            for (int x = 0; x < TOTROUNDS_500; x++) {
                strBuf.append("or");
                strBuf.append('\t');
                strBuf.append(size);
                strBuf.append('\t');
                strBuf.append(sched);
                strBuf.append('\t');
                strBuf.append(x * 500);
                strBuf.append('\t');
                strBuf.append(stats[(int) java.lang.Math.log10(size)][sched][x]);
                strBuf.append('\n');
            }

            LOGGER.info(strBuf.toString());*/
        }

        final StringBuffer strBuf = new StringBuffer(60);
        strBuf.append('\n');
        strBuf.append("prot");
        strBuf.append('\t');
        strBuf.append("size");
        strBuf.append('\t');
        strBuf.append("round");
        strBuf.append('\t');
        for (int sched = SchedulerFactory.TRANSMAP; sched < SchedulerFactory.STATE; sched++) {
            strBuf.append('\t');
            strBuf.append(sched);
        }
        strBuf.append('\n');

        // Print statistics
        for (int x = 0; x < TOTROUNDS_500; x++) {
            strBuf.append("or");
            strBuf.append('\t');
            strBuf.append(size);
            strBuf.append('\t');
            strBuf.append(x * TOTROUNDS_500);
            for (int sched = SchedulerFactory.TRANSMAP; sched < SchedulerFactory.STATE; sched++) {
                strBuf.append('\t');
                strBuf.append(stats[(int) java.lang.Math.log10(size)][sched][x]);
            }
            strBuf.append('\n');
        }
        logger.info(strBuf.toString());


    }

}
