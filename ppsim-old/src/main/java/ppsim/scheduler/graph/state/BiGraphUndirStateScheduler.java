/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  30 Σεπ 2011 4:04:05 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.state;

import ppsim.scheduler.graph.BiGraphUndirGenerator;

/**
 * Implements scheduler based on an Undirected Bipartite Graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class BiGraphUndirStateScheduler<State> extends AbstractGraphStateScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new BiGraphUndirGenerator().generate(this);
    }

}
