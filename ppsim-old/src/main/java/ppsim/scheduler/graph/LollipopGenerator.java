package ppsim.scheduler.graph;

/**
 * Generates Lollipop Graphs.
 */
public class LollipopGenerator {

    /**
     * Graph generator.
     *
     * @param scheduler the Graph Scheduler that stores the graph properties.
     * @param directed  true if the graph has directed edges, otherwise false.
     * @param mParam the size of the clique
     */
    public void generate(final GraphScheduler scheduler, final boolean directed, final int mParam) {
        final int totAgents = scheduler.size();
        int cliqueAgents = (int) java.lang.Math.sqrt(totAgents);

        // Check m parameter
        if (mParam > 0) {
            cliqueAgents = mParam;
        }

        // Iterate through all agents acting as initiators
        for (int i = 0; i < cliqueAgents; i++) {
            // Iterate through all agents acting as responders
            for (int j = i + 1; j < cliqueAgents; j++) {

                // Make sure it is not the same agent
                if (i == j) {
                    continue;
                }

                // Create edge
                scheduler.setupEdge(i, j);

                if (!directed) {
                    // Also add reverse edge
                    scheduler.setupEdge(j, i);
                }
            }
        }

        // Connect remaining agents
        for (int i = cliqueAgents; i < totAgents; i++) {
            // Create edge
            scheduler.setupEdge(i, i - 1);

            if (!directed) {
                // Also add reverse edge
                scheduler.setupEdge(i - 1, i);
            }
        }
    }
}
