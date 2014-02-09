/**
* Computer Engineering and Informatics Department, University of Patras
*
* Project:     Undergraduate diploma thesis
* Created at:  28 Αυγ 2011 3:56:05 μμ
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
public class TwoCycleExperimentA extends AbstractExperiment<TwoCycleState, TwoCycleProtocol>  {

    /**
     * Default constructor.
     *
     * @param scheduler the Scheduler that will be used in the experiment.
     * @param size      the number of agents.
     */
    public TwoCycleExperimentA(final Scheduler<TwoCycleState> scheduler, final int size) {
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
        final double condition = 0.8*getPopulationSize();

        return (popLD + popFD >= condition);
    }

    /**
     * Finalizes the execution of the experiment.
     */
    protected void completeExperiment() {
        final long popFD = getPopulation().getStateCount(TwoCycleState.STATE_FD);
        final long popLD = getPopulation().getStateCount(TwoCycleState.STATE_LD);
        final double condition = 0.8*getPopulationSize();

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

        long[][] stats = new long[(int) java.lang.Math.log10(EXPSIZE_LARGE) + 1][SchedulerFactory.TOTAL];
        final SchedulerFactory<TwoCycleState> sfactory = new SchedulerFactory<TwoCycleState>();

        for (int size = EXPSIZE_SMALL; size < EXPSIZE_LARGE; size *= EXPSIZE_STEP)
        {
            //final int size = 10;
            //for (int sched = 7; sched < 15; sched++) {

                final int sched = SchedulerFactory.GNP_EDGE;

                    try {
                        // Instantiate the new scheduler
                        final Scheduler<TwoCycleState> scheduler = sfactory.createScheduler(sched);

                        // Construct new ppsim.model.AbstractExperiment
                        final TwoCycleExperimentA exp = new TwoCycleExperimentA(scheduler, size);

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
                    strBuf.append("TwoCycle");
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
