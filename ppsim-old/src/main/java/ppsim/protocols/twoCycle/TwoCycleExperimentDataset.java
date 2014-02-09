/**
* Computer Engineering and Informatics Department, University of Patras
*
* Project:     Undergraduate diploma thesis
* Created at:  11 Οκτ 2011 4:41:00 μμ
* Author:      Theofanis Raptis
*
*/

package ppsim.protocols.twoCycle;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
* Implementation of an experiment of the TwoCycle protocol.
*/
public class TwoCycleExperimentDataset extends AbstractExperiment<TwoCycleState, TwoCycleProtocol> {

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     */
    public TwoCycleExperimentDataset(final Scheduler<TwoCycleState> scheduler, final int size) {
        super(size, new TwoCycleProtocol(), scheduler);
        LOGGER.debug("TwoCycleExperimentA(" + getSchedulerName() + ")<" + size + "> initialized");
    }

    /**
     * Initialize the population.
     */
    protected void initPopulation() {
        for (int i = 0; i < getPopulationSize(); i++) {
            new TwoCycleAgent(getPopulation()); //NOPMD
        }
    }

    /**
     * Evaluates if the population protocol has reached a stable state.
     *
     * @return true if the protocol has reached a stable state.
     */
    protected boolean checkStability() {
        final long popFD = getPopulation().getStateCount(TwoCycleState.STATE_FD);
        final long popLD = getPopulation().getStateCount(TwoCycleState.STATE_LD);
        final double condition = 0.7*getPopulationSize();

        return (popLD + popFD >= condition);
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        final long popFD = getPopulation().getStateCount(TwoCycleState.STATE_FD);
        final long popLD = getPopulation().getStateCount(TwoCycleState.STATE_LD);
        final double condition = 0.7*getPopulationSize();

        if (popLD + popFD >= condition) {
            LOGGER.warn("TwoCycleExperiment(" + getSchedulerName() + ")<" + getPopulationSize() + ","
                    + "> completed at round " + getRound() + " -- graph has at least one 2-cycle");
        }
    }


    /**
     * Main function.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {

        final Logger logger = Logger.getLogger("");

        long[][] stats = new long[(int) java.lang.Math.log10(DATASET_SIZE) + 1][SchedulerFactory.TOTAL];
        final SchedulerFactory<TwoCycleState> sfactory = new SchedulerFactory<TwoCycleState>();

        final int size = DATASET_SIZE;
        final int sched = SchedulerFactory.RANDOM_EDGE_FROM_DATASET;

                    try {
                        // Instantiate the new scheduler
                        final Scheduler<TwoCycleState> scheduler = sfactory.createScheduler(sched);

                        // Construct new ppsim.model.AbstractExperiment
                        final TwoCycleExperimentDataset exp = new TwoCycleExperimentDataset(scheduler, size);

                        // Start the experiment
                        exp.run();

                        // Keep track of rounds
                        stats[(int) java.lang.Math.log10(size)][sched] += exp.getRound();

                    } catch (Exception ex) {
                        logger.error("Error occured", ex);
                    }

                // Average out repetitions
                stats[(int) java.lang.Math.log10(size)][sched] /= EXPREPETITIONS;


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

            final int thisSize = DATASET_SIZE;
            strBuf.append("TwoCycle");
            strBuf.append('\t');
            strBuf.append(thisSize);
            strBuf.append('\t');
            strBuf.append(sched);
            strBuf.append('\t');
            strBuf.append(stats[(int) java.lang.Math.log10(thisSize)][sched]);
            strBuf.append('\n');

            logger.info(strBuf.toString());
        }
}
