/**
* Computer Engineering and Informatics Department, University of Patras
*
* Project:     Undergraduate diploma thesis
* Created at:  11 Οκτ 2011 4:25:13 μμ
* Author:      Theofanis Raptis
*
*/

package ppsim.protocols.uStar;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
* Implementation of an experiment of the OR protocol given a dataset.
*/
public class UstarExperimentDataset extends AbstractExperiment<UstarState, UstarProtocol> {

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     */
    public UstarExperimentDataset(final Scheduler<UstarState> scheduler, final int size) {
        super(size, new UstarProtocol(), scheduler);
        LOGGER.debug("UstarExperimentA(" + getSchedulerName() + ")<" + size + "> initialized");
    }

    /**
     * Initialize the population.
     */
    protected void initPopulation() {
        for (int i = 0; i < getPopulationSize(); i++) {
            new UstarAgent(getPopulation()); //NOPMD
        }
    }


    /**
     * Evaluates if the population protocol has reached a stable state.
     *
     * @return true if the protocol has reached a stable state.
     */
    protected boolean checkStability() {
        final long popZ = getPopulation().getStateCount(UstarState.STATE_Z);
        final double limit = 0.7*getPopulationSize();
        return (popZ >= limit);
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        final long popZ = getPopulation().getStateCount(UstarState.STATE_Z);
        final double limit = 0.7*getPopulationSize();

        if (popZ >= limit) {
            LOGGER.warn("UstarExperiment(" + getSchedulerName() + ")<" + getPopulationSize() + ","
                    + "> completed at round " + getRound() + " -- graph is an undirected star");
        }
    }

    /**
     * Main function.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger("ppsim.protocols.uStar");

        long[][] stats = new long[(int) java.lang.Math.log10(DATASET_SIZE) + 1][SchedulerFactory.TOTAL];
        final SchedulerFactory<UstarState> sfactory = new SchedulerFactory<UstarState>();

            final int size = DATASET_SIZE;
            final int sched = SchedulerFactory.RANDOM_EDGE_FROM_DATASET;
            try {
                // Instantiate the new scheduler
                final Scheduler<UstarState> scheduler = sfactory.createScheduler(sched);

                // Construct new ppsim.model.AbstractExperiment
                final UstarExperimentDataset exp = new UstarExperimentDataset(scheduler, size);

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
            strBuf.append("Ustar");
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
