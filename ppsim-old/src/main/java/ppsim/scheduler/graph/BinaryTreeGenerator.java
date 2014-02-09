/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 7:05:27 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph;

/**
 * Generates Binary Trees.
 */
public class BinaryTreeGenerator {

     /**
     * Graph generator.
     *
     * @param scheduler the Graph Scheduler that stores the graph properties.
     * @param directed  true if the graph has directed edges, otherwise false.
     */
    public void generate(final GraphScheduler scheduler, final boolean directed) {
        final int totAgents = scheduler.size();
        int parent;

        //root=0
        // Iterate through all agents acting as responders
        for (int i = totAgents - 1; i > 0; i--) {

            //integer division in Java results in only the whole number part of the result,
            //any fractional part is dropped
            parent = (i-1)/2;

            // Create edge
            scheduler.setupEdge(parent, i);

            if (!directed) {
                // Also add reverse edge
                scheduler.setupEdge(i, parent);
            }
        }
    }
}
