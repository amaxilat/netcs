/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 9:23:59 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.edge;

import ppsim.scheduler.graph.StarGenerator;

/**
 * Implements scheduler based on a Star connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class StarEdgeScheduler<State> extends AbstractGraphEdgeScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new StarGenerator().generate(this, false);
    }

}
