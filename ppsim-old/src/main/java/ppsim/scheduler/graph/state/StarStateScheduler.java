/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 9:27:37 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.state;

import ppsim.scheduler.graph.StarGenerator;

/**
 * Implements scheduler based on a Star connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class StarStateScheduler<State> extends AbstractGraphStateScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new StarGenerator().generate(this, false);
    }

}
