/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  21 Αυγ 2011 11:50:54 πμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph;

import java.lang.Math;

/**
 * Generates Tournaments.
 */
public class TournamentGenerator {

    /**
     * Graph generator.
     *
     * @param scheduler the Graph Scheduler that stores the graph properties.
     * @param directed  true if the graph has directed edges, otherwise false.
     */
    public void generate(final GraphScheduler scheduler, final boolean directed) {
        final int totAgents = scheduler.size();

        // Iterate through all agents acting as initiators
        for (int i = 1; i < totAgents; i++) {
            for (int j = 0; j < i; j++) {
            // Create edge
            double coin = Math.random();
            if (coin < 0.8){
                scheduler.setupEdge(i, j);
            }
            else{
                scheduler.setupEdge(j, i);
            }
            //if (!directed) -- tournaments are directed
            }

        }
    }

}
