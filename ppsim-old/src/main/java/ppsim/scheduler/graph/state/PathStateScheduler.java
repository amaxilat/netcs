/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 1:25:56 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.state;

import ppsim.scheduler.graph.PathGenerator;

/**
 * Implements scheduler based on a Path connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class PathStateScheduler<State> extends AbstractGraphStateScheduler<State>  {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new PathGenerator().generate(this, false);
    }

}
