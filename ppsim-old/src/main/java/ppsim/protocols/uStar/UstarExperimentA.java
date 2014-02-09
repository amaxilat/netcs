/**
* Computer Engineering and Informatics Department, University of Patras
*
* Project:     Undergraduate diploma thesis
* Created at:  20 Αυγ 2011 9:40:41 μμ
* Author:      Theofanis Raptis
*
*/

package ppsim.protocols.uStar;

import org.apache.log4j.Logger;
import ppsim.model.AbstractExperiment;
import ppsim.model.Scheduler;
import ppsim.scheduler.SchedulerFactory;

/**
* Implementation of an experiment of the Undirected-Star protocol.
*/
public class UstarExperimentA extends AbstractExperiment<UstarState, UstarProtocol> {

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     */
    public UstarExperimentA(final Scheduler<UstarState> scheduler, final int size) {
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
        final long limit = getPopulationSize();

        return (popZ >= limit);
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        final long popZ = getPopulation().getStateCount(UstarState.STATE_Z);
        final double limit = getPopulationSize();

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

        final Logger logger = Logger.getLogger("");

        long[][] stats = new long[(int) java.lang.Math.log10(EXPSIZE_LARGE) + 1][SchedulerFactory.TOTAL];
        final SchedulerFactory<UstarState> sfactory = new SchedulerFactory<UstarState>();

        for (int size = EXPSIZE_SMALL; size < EXPSIZE_LARGE; size *= EXPSIZE_STEP)
        {
            //final int size = 10;
            //for (int sched = 7; sched < 15; sched++) {

                final int sched = SchedulerFactory.GNP_EDGE;

                    try {
                        // Instantiate the new scheduler
                        final Scheduler<UstarState> scheduler = sfactory.createScheduler(sched);

                        // Construct new ppsim.model.AbstractExperiment
                        final UstarExperimentA exp = new UstarExperimentA(scheduler, size);

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

            for (int thisSize = EXPSIZE_SMALL; thisSize < EXPSIZE_LARGE; thisSize *= EXPSIZE_STEP)
            {
                //final int thisSize = 1000;
                for (int schedul = 3; schedul < 4; schedul++) {
                    strBuf.append("Ustar");
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
