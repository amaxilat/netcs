/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Οκτ 2011 2:37:13 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph.edge;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ppsim.scheduler.graph.DatasetRandomGraphGenerator;

/**
 * Implements scheduler based on a realistic random graph
 * that models the real scenario from a given dataset.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class DatasetRandomGraphEdgeScheduler<State> extends AbstractGraphEdgeScheduler<State> {


    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        try {
            new DatasetRandomGraphGenerator().generate(this);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatasetRandomGraphEdgeScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
