/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Αυγ 2011 10:31:52 πμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph;

/**
 * Generates Path Graphs.
 */
public class PathGenerator {

     /**
     * Graph generator.
     *
     * @param scheduler the Graph Scheduler that stores the graph properties.
     * @param directed  true if the graph has directed edges, otherwise false.
     */
    public void generate(final GraphScheduler scheduler, final boolean directed) {
        final int totAgents = scheduler.size();

        // Iterate through all agents acting as initiators
        for (int i = 0; i < totAgents - 1; i++) {
            // Create edge
            scheduler.setupEdge(i, i + 1);

            if (!directed) {
                // Also add reverse edge
                scheduler.setupEdge(i + 1, i);
            }
        }
    }

}
