/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 9:46:46 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.state;

import ppsim.scheduler.graph.CycleGenerator;

/**
 * Implements scheduler based on a Cycle connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class CycleStateScheduler<State> extends AbstractGraphStateScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new CycleGenerator().generate(this, false);
    }

}
