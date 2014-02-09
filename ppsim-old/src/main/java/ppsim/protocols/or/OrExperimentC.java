package ppsim.protocols.or;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
* Implementation of an experiment of the OR protocol.
*/
public class OrExperimentC extends AbstractExperiment<Boolean, OrProtocol> {

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     */
    public OrExperimentC(final Scheduler<Boolean> scheduler, final int size) {
        super(size, new OrProtocol(), scheduler);
        LOGGER.debug("OrExperimentC(" + getSchedulerName() + ")<" + size + "> initialized");
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
        final long target = getPopulationSize();
        return getPopulation().getStateCount(true) >= target;
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        LOGGER.warn("OrExperimentC(" + getSchedulerName() + ")<" + getPopulationSize()
                + "> completed at round " + getRound());
    }

    /**
     * Main function.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger("ppsim.protocols.or");
        final int[] mParam = {800, 900, 950, 1000, 5};
        long[][] stats = new long[11][3];
        final int size = 1000;
        final SchedulerFactory<Boolean> sfactory = new SchedulerFactory<Boolean>();

        for (int m = 0; m < mParam.length; m++) {
            for (int sched = SchedulerFactory.RANDOM; sched < SchedulerFactory.GNP_EDGE; sched++) {
                for (int repetition = 0; repetition < EXPREPETITIONS; repetition++) {
                    try {
                        // Instantiate the new scheduler
                        final Scheduler<Boolean> scheduler = sfactory.createScheduler(sched);

                        // Construct new ppsim.model.AbstractExperiment
                        final OrExperimentC exp = new OrExperimentC(scheduler, size);

                        // Start the experiment
                        exp.run();

                        // Keep track of rounds
                        stats[m][sched] += exp.getRound();

                    } catch (Exception ex) {
                        logger.error("Error occured", ex);
                    }
                }

                // Average out repetitions
                stats[m][sched] /= EXPREPETITIONS;
/*
                final StringBuffer strBuf = new StringBuffer();
                strBuf.append('\n');
                strBuf.append("prot");
                strBuf.append('\t');
                strBuf.append("size");
                strBuf.append('\t');
                strBuf.append("sched");
                strBuf.append('\t');
                strBuf.append("m");
                strBuf.append('\t');
                strBuf.append("round");
                strBuf.append('\n');

                // Print statistics
                strBuf.append("or");
                strBuf.append('\t');
                strBuf.append(size);
                strBuf.append('\t');
                strBuf.append(sched);
                strBuf.append('\t');
                strBuf.append(thisM);
                strBuf.append('\t');
                strBuf.append(stats[m][sched]);
                strBuf.append('\n');

                LOGGER.info(strBuf.toString());*/
            }
        }

        final StringBuffer strBuf = new StringBuffer(60);
        strBuf.append('\n');
        strBuf.append("prot");
        strBuf.append('\t');
        strBuf.append('m');
        strBuf.append('\t');
        strBuf.append("sched");
        strBuf.append('\t');
        strBuf.append("round");
        strBuf.append('\n');

        // Print statistics
        for (int m = 0; m < mParam.length; m++) {
            final int thisM = mParam[m];
            for (int sched = SchedulerFactory.RANDOM; sched < SchedulerFactory.GNP_EDGE; sched++) {

                strBuf.append("or");
                strBuf.append('\t');
                strBuf.append(thisM);
                strBuf.append('\t');
                strBuf.append(sched);
                strBuf.append('\t');
                strBuf.append(stats[m][sched]);
                strBuf.append('\n');
            }
        }

        logger.info(strBuf.toString());
    }

}
