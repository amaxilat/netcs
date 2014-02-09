/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 9:20:44 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph;

/**
 * Generates Star Graphs.
 */
public class StarGenerator {

     /**
     * Graph generator.
     *
     * @param scheduler the Graph Scheduler that stores the graph properties.
     * @param directed  true if the graph has directed edges, otherwise false.
     */
    public void generate(final GraphScheduler scheduler, final boolean directed) {
        final int totAgents = scheduler.size();

        // Iterate through all agents acting as responders
        for (int i = 1; i < totAgents; i++) {
            // Create edge
            scheduler.setupEdge(0, i);

            if (!directed) {
                // Also add reverse edge
                scheduler.setupEdge(i, 0);
            }
        }
    }
}
