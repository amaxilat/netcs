/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  30 Σεπ 2011 4:01:47 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.node;

import ppsim.scheduler.graph.BiGraphUndirGenerator;

public class BiGraphUndirNodeScheduler<State> extends AbstractGraphNodeScheduler<State>  {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new BiGraphUndirGenerator().generate(this);
    }

}
