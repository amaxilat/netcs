package ppsim.protocols.le;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
 * Implementation of an experiment of the Leader Election protocol.
 */
public class LEExperimentB extends AbstractExperiment<Boolean, LEProtocol> {

    /**
     * Number of rounds before producing statistics.
     */
    public static final int TOTROUNDS_500 = 500000;

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
    public LEExperimentB(final Scheduler<Boolean> scheduler, final int size) {
        super(size, new LEProtocol(), scheduler);
        LOGGER.debug("LEExperimentB(" + getSchedulerName() + ")<" + size + "> initialized");
    }

    /**
     * Initialize the population.
     */
    protected void initPopulation() {
        for (int i = 0; i < getPopulationSize(); i++) {
            new LEAgent(getPopulation()); //NOPMD
        }
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
            if (thisSlot > TOTROUNDS_500 - 1) {
                return trueState == 1;
            }
            expStats[thisSlot] = trueState;
        }

        return trueState == 1;
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        LOGGER.warn("LEExperimentB(" + getSchedulerName() + ")<" + getPopulationSize()
                + "> completed at round " + getRound());
    }

    /**
     * Main function.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger("ppsim.protocols.le");
        long[][][] stats = new long[(int) java.lang.Math.log10(EXPSIZE_LARGE) + 1][SchedulerFactory.TOTAL][TOTROUNDS_500];
        final int size = 10;
        final SchedulerFactory<Boolean> sfactory = new SchedulerFactory<Boolean>();

        for (int sched = SchedulerFactory.GNP_EDGE;
             sched < SchedulerFactory.LOLLIPOP_EDGE; sched++) {
            for (int repetition = 0; repetition < EXPREPETITIONS; repetition++) {
                try {
                    // Instantiate the new scheduler
                    final Scheduler<Boolean> scheduler = sfactory.createScheduler(sched);

                    // Construct new ppsim.model.AbstractExperiment
                    final LEExperimentB exp = new LEExperimentB(scheduler, size);

                    // Start the experiment
                    exp.run();

                    // Copy statistics
                    for (int x = 0; x < TOTROUNDS_500; x++) {
                        stats[(int) java.lang.Math.log10(size)][sched][x] += exp.expStats[x];
                    }

                    // Copy last value to remaining slots
                    final int finalSlot = (int) exp.getRound() / TOTROUNDS_500;
                    for (int x = finalSlot + 1; x < TOTROUNDS_500; x++) {
                        stats[(int) java.lang.Math.log10(size)][sched][x] += 1;
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
        for (int sched = SchedulerFactory.GNP_EDGE;
             sched < SchedulerFactory.LOLLIPOP_EDGE; sched++) {
            strBuf.append('\t');
            strBuf.append(sched);
        }
        strBuf.append('\n');

        // Print statistics
        for (int x = 0; x < TOTROUNDS_500; x++) {
            strBuf.append("le");
            strBuf.append('\t');
            strBuf.append(size);
            strBuf.append('\t');
            strBuf.append(x * TOTROUNDS_500);
            int tot = 0;
            for (int sched = SchedulerFactory.GNP_EDGE;
                 sched < SchedulerFactory.LOLLIPOP_EDGE; sched++) {
                strBuf.append('\t');
                strBuf.append(stats[(int) java.lang.Math.log10(size)][sched][x]);
                tot += stats[(int) java.lang.Math.log10(size)][sched][x];
            }
            if (tot <= 3) {
                break;
            }
            strBuf.append('\n');
        }
        logger.info(strBuf.toString());
    }

}
