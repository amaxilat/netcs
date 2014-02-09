/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  21 Αυγ 2011 1:42:55 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.edge;

import ppsim.scheduler.graph.TournamentGenerator;

/**
 * Implements scheduler based on a Tournament.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class TournamentEdgeScheduler<State> extends AbstractGraphEdgeScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new TournamentGenerator().generate(this, true);
    }
}
