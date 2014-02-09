/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 7:34:01 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.edge;

import ppsim.scheduler.graph.BinaryTreeGenerator;

/**
 * Implements scheduler based on a Binary Tree connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class BinaryTreeEdgeScheduler<State> extends AbstractGraphEdgeScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new BinaryTreeGenerator().generate(this, true);
    }

}
