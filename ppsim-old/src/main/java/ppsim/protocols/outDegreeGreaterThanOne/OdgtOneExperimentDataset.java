/**
* Computer Engineering and Informatics Department, University of Patras
*
* Project:     Undergraduate diploma thesis
* Created at:  11 Οκτ 2011 4:34:32 μμ
* Author:      Theofanis Raptis
*
*/

package ppsim.protocols.outDegreeGreaterThanOne;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
* Implementation of an experiment of the OR protocol given a dataset.
*/
public class OdgtOneExperimentDataset extends AbstractExperiment<OdgtOneState, OdgtOneProtocol> {

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     */
    public OdgtOneExperimentDataset(final Scheduler<OdgtOneState> scheduler, final int size) {
        super(size, new OdgtOneProtocol(), scheduler);
        LOGGER.debug("OdgtOneExperimentA(" + getSchedulerName() + ")<" + size + "> initialized");
    }

    /**
     * Initialize the population.
     */
    protected void initPopulation() {
        for (int i = 0; i < getPopulationSize(); i++) {
            new OdgtOneAgent(getPopulation()); //NOPMD
        }
    }

    /**
     * Evaluates if the population protocol has reached a stable state.
     *
     * @return true if the protocol has reached a stable state.
     */
    protected boolean checkStability() {
        final long popINIT = getPopulation().getStateCount(OdgtOneState.STATE_INIT);
        final long popI = getPopulation().getStateCount(OdgtOneState.STATE_I);
        final long popR = getPopulation().getStateCount(OdgtOneState.STATE_R);
        final long popY = getPopulation().getStateCount(OdgtOneState.STATE_Y);
        final double limit = getPopulationSize();

        LOGGER.debug("INIT=" + popINIT + ", I=" + popI + ", R=" + popR + ", Y=" + popY);
        return (popY >= limit);
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        final long popINIT = getPopulation().getStateCount(OdgtOneState.STATE_INIT);
        final long popI = getPopulation().getStateCount(OdgtOneState.STATE_I);
        final long popR = getPopulation().getStateCount(OdgtOneState.STATE_R);
        final long popY = getPopulation().getStateCount(OdgtOneState.STATE_Y);

        final double limit = (popINIT + popI + popR);

        if (popY > limit) {
            LOGGER.warn("OdgtOneExperiment(" + getSchedulerName() + ")<" + getPopulationSize() + ","
                    + "> completed at round " + getRound() + " -- in degree greater than 1");
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
        final SchedulerFactory<OdgtOneState> sfactory = new SchedulerFactory<OdgtOneState>();

            final int size = DATASET_SIZE;
            final int sched = SchedulerFactory.DATASET;

                    try {
                        // Instantiate the new scheduler
                        final Scheduler<OdgtOneState> scheduler = sfactory.createScheduler(sched);

                        // Construct new ppsim.model.AbstractExperiment
                        final OdgtOneExperimentDataset exp = new OdgtOneExperimentDataset(scheduler, size);

                        // Start the experiment
                        exp.runForDataset();

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

            strBuf.append("OdgtOne");
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
