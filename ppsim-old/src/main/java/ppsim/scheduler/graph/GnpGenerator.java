package ppsim.scheduler.graph;

import java.util.Random;

/**
 * Generates G<sub>n,p</sub> graphs.
 */
public class GnpGenerator {

    /**
     * Connectivity threshold for G_np graphs.
     */
    public static final double CONNECTIVITY_THR = 1.05;

    /**
     * Graph Generator.
     *
     * @param scheduler the Graph Scheduler that stores the graph properties.
     * @param directed  true if the graph has directed edges, otherwise false.
     */
    public void generate(final GraphScheduler scheduler, final boolean directed) {
        final Random randomGenerator = new Random();
        final int totAgents = scheduler.size();
        //final double edgeProbability = CONNECTIVITY_THR * java.lang.Math.log(totAgents) / totAgents;
        final double edgeProbability = 0.5;
        final boolean[] connected = new boolean[totAgents];

        // Iterate through all agents acting as initiators
/*        for (int i = 0; i < totAgents; i++) {

            // Iterate through all agents acting as responders
            for (int j = 0; j < totAgents; j++) {

                // Make sure it is not the same agent
                if (i == j) {
                    continue;
                }

                // Sample Edge
                if (edgeProbability >= randomGenerator.nextDouble()) {
                    // Create edge
                    scheduler.setupEdge(i, j);
                    connected[i] = true;

                    if (!directed) {
                        // Also add reverse edge
                        scheduler.setupEdge(j, i);
                        connected[j] = true;
                    }
                }
            }
        }

*/

        //leave only one 2cycle.
        //worst case equivalent for the 2cycle protocol

        // Iterate through all agents acting as initiators
        for (int i = 0; i < totAgents - 1; i++) {

            // Iterate through all agents acting as responders
            for (int j = i+1; j < totAgents; j++) {

                // Sample Edge
                if (edgeProbability >= randomGenerator.nextDouble()) {
                    // Create edge
                    scheduler.setupEdge(i, j);
                    connected[i] = true;

                    if (!directed) {
                        // Also add reverse edge
                        scheduler.setupEdge(j, i);
                        connected[j] = true;
                    }
                }
            }
        }

        //leave one single 2cycle
        scheduler.setupEdge(0, 1);
        scheduler.setupEdge(1, 0);



        // Make sure first agent is connected
        if (!connected[0]) {
            // connect with second agent
            scheduler.setupEdge(0, 1);

            if (!directed) {
                // Also add reverse edge
                scheduler.setupEdge(1, 0);
            }
        }

        // Make sure all nodes are connected
        for (int i = 1; i < totAgents; i++) {
            if (!connected[i]) {
                // connect with first and second agent
                scheduler.setupEdge(i, 0);

                if (!directed) {
                    // Also add reverse edge
                    scheduler.setupEdge(0, i);
                }
            }
        }
    }

}
