/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 8:56:12 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.node;

import ppsim.scheduler.graph.BinaryTreeGenerator;

/**
 * Implements scheduler based on a Binary Tree connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class BinaryTreeNodeScheduler<State> extends AbstractGraphNodeScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new BinaryTreeGenerator().generate(this, false);
    }

}
