/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 1:17:30 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.node;

import ppsim.scheduler.graph.PathGenerator;

/**
 * Implements scheduler based on a Path connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class PathNodeScheduler<State> extends AbstractGraphNodeScheduler<State>  {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new PathGenerator().generate(this, false);
    }

}
