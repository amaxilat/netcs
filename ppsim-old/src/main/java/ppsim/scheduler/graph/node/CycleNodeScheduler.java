/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 9:45:45 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.node;

import ppsim.scheduler.graph.CycleGenerator;

/**
 * Implements scheduler based on a Cycle connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class CycleNodeScheduler<State> extends AbstractGraphNodeScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new CycleGenerator().generate(this, false);
    }

}
