/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  30 Σεπ 2011 3:56:17 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.edge;

import ppsim.scheduler.graph.BiGraphUndirGenerator;

/**
 * Implements scheduler based on an Undirected Bipartite Graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class BiGraphUndirEdgeScheduler<State> extends AbstractGraphEdgeScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new BiGraphUndirGenerator().generate(this);
    }

}
