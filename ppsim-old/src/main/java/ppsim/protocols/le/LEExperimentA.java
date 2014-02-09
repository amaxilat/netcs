package ppsim.protocols.le;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
 * Implementation of an experiment of the Leader Election protocol.
 */
public class LEExperimentA extends AbstractExperiment<Boolean, LEProtocol> {

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     */
    public LEExperimentA(final Scheduler<Boolean> scheduler, final int size) {
        super(size, new LEProtocol(), scheduler);
        LOGGER.debug("LEExperimentA(" + getSchedulerName() + ")<" + size + "> initialized");
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
        return getPopulation().getStateCount(true) == 1;
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        LOGGER.warn("LEExperimentA(" + getSchedulerName() + ")<" + getPopulationSize()
                + "> completed at round " + getRound());
    }

    /**
     * Main function.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger("ppsim.protocols.le");

        long[][] stats = new long[(int) java.lang.Math.log10(EXPSIZE_LARGE) + 1][SchedulerFactory.TOTAL];
        final SchedulerFactory<Boolean> sfactory = new SchedulerFactory<Boolean>();

        for (int size = EXPSIZE_SMALL; size < EXPSIZE_LARGE; size *= EXPSIZE_STEP) {
            //for (int sched = SchedulerFactory.GNP_NODE; sched < SchedulerFactory.REALITY; sched++) {


            final int sched = SchedulerFactory.STATE;
            for (int repetition = 0; repetition < EXPREPETITIONS; repetition++) {
                try {
                    // Instantiate the new scheduler
                    final Scheduler<Boolean> scheduler = sfactory.createScheduler(sched);

                    // Construct new ppsim.model.AbstractExperiment
                    final LEExperimentA exp = new LEExperimentA(scheduler, size);

                    // Start the experiment
                    exp.run();

                    // Keep track of rounds
                    stats[(int) java.lang.Math.log10(size)][sched] += exp.getRound();

                } catch (Exception ex) {
                    logger.error("Error occured", ex);
                }
            }

            // Average out repetitions
            stats[(int) java.lang.Math.log10(size)][sched] /= EXPREPETITIONS;
            //}


            final StringBuffer strBuf = new StringBuffer(60);
            strBuf.append('\n');
            strBuf.append("prot");
            strBuf.append('\t');
            strBuf.append("size");
            strBuf.append('\t');
            strBuf.append("sched");
            strBuf.append('\t');
            strBuf.append("rounds");
            strBuf.append('\n');

            for (int thisSize = EXPSIZE_SMALL; thisSize < EXPSIZE_LARGE; thisSize *= EXPSIZE_STEP) {
                //for (int sched = SchedulerFactory.GNP_NODE; sched < SchedulerFactory.REALITY; sched++) {
                for (int schedul = 2; schedul < 3; schedul++) {
                    strBuf.append("le");
                    strBuf.append('\t');
                    strBuf.append(thisSize);
                    strBuf.append('\t');
                    strBuf.append(sched);
                    strBuf.append('\t');
                    strBuf.append(stats[(int) java.lang.Math.log10(thisSize)][sched]);
                    strBuf.append('\n');
                }
            }

            logger.info(strBuf.toString());
        }
    }

}
