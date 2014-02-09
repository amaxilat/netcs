package ppsim.scheduler.graph;

/**
 * Generates 2D Grid Graphs.
 */
public class Grid2DGenerator {

    /**
     * Generate the graph.
     *
     * @param scheduler the Graph Scheduler that stores the graph properties.
     * @param directed  true if the graph has directed edges, otherwise false.
     */
    public void generate(final GraphScheduler scheduler, final boolean directed) {
        // Compute grid length
        final int length = (int) java.lang.Math.sqrt(scheduler.size());

        // Iterate through y coordinate
        for (int coordY = 0; coordY < length; coordY++) {
            // Iterate through x coordinate
            for (int coordX = 0; coordX < length; coordX++) {
                final int idInit = calcPos(length, coordX, coordY);

                // first X node
                if (coordX == 0) {
                    // Create edge with next
                    scheduler.setupEdge(idInit, calcPos(length, coordX + 1, coordY));

                } else if (coordX == length - 1) {
                    // last node
                    if (!directed) {
                        scheduler.setupEdge(idInit, calcPos(length, coordX - 1, coordY));
                    }

                } else {
                    // intermediate node
                    scheduler.setupEdge(idInit, calcPos(length, coordX + 1, coordY));

                    if (!directed) {
                        // Also add edge with previous node
                        scheduler.setupEdge(idInit, calcPos(length, coordX - 1, coordY));
                    }
                }

                // first Y node
                if (coordY == 0) {
                    // Create edge with next
                    scheduler.setupEdge(idInit, calcPos(length, coordX, coordY + 1));

                } else if (coordY == length - 1) {
                    // last node
                    if (!directed) {
                        scheduler.setupEdge(idInit, calcPos(length, coordX, coordY - 1));
                    }

                } else {
                    // intermediate node
                    scheduler.setupEdge(idInit, calcPos(length, coordX, coordY + 1));

                    if (!directed) {
                        // Also add edge with previous node
                        scheduler.setupEdge(idInit, calcPos(length, coordX, coordY - 1));
                    }
                }
            }
        }

        // Connect any remaining agents with the first agent
        for (int i = length * length; i < scheduler.size(); i++) {
            scheduler.setupEdge(i, 0);

            if (!directed) {
                // Also add reverse edge
                scheduler.setupEdge(0, i);
            }
        }
    }

    /**
     * Calculate position of agent given coordinates.
     *
     * @param size   of 2D grid.
     * @param coordX the x coordinate of the agent.
     * @param coordY the y coordinate of the agent.
     * @return the position of the agent in the array list.
     */
    private int calcPos(final int size, final int coordX, final int coordY) {
        return coordX + coordY * size;
    }
}
