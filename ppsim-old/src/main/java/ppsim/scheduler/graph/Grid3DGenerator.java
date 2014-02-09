package ppsim.scheduler.graph;

/**
 * Generates 3D Grid Graphs.
 */
public class Grid3DGenerator {

    /**
     * Graph Generator.
     *
     * @param scheduler the Graph Scheduler that stores the graph properties.
     * @param directed  true if the graph has directed edges, otherwise false.
     */
    public void generate(final GraphScheduler scheduler, final boolean directed) {
        // Compute grid length
        final int length = (int) java.lang.Math.cbrt(scheduler.size());

        // Iterate through z coordinate
        for (int coordZ = 0; coordZ < length; coordZ++) {
            // Iterate through y coordinate
            for (int coordY = 0; coordY < length; coordY++) {
                // Iterate through x coordinate
                for (int coordX = 0; coordX < length; coordX++) {
                    final int idInit = calcPos(length, coordX, coordY, coordZ);

                    // first X node
                    if (coordX == 0) {
                        // Create edge with next
                        scheduler.setupEdge(idInit, calcPos(length, coordX + 1, coordY, coordZ));

                        /*} else if (coordX == length - 1) {
                           // last node
                           //setupEdge(idInit, calcPos(length, coordX - 1, coordY, coordZ));

                       } */
                    } else {
                        // intermediate node
                        scheduler.setupEdge(idInit, calcPos(length, coordX + 1, coordY, coordZ));
                        //setupEdge(idInit, calcPos(length, coordX - 1, coordY, coordZ));
                    }

                    // first Y node
                    if (coordY == 0) {
                        // Create edge with next
                        scheduler.setupEdge(idInit, calcPos(length, coordX, coordY + 1, coordZ));

                    } else if (coordY == length - 1) {
                        // last node
                        if (!directed) {
                            scheduler.setupEdge(idInit, calcPos(length, coordX, coordY - 1, coordZ));
                        }

                    } else {
                        // intermediate node
                        scheduler.setupEdge(idInit, calcPos(length, coordX, coordY + 1, coordZ));

                        if (!directed) {
                            scheduler.setupEdge(idInit, calcPos(length, coordX, coordY - 1, coordZ));
                        }
                    }

                    // first Z node
                    if (coordZ == 0) {
                        // Create edge with next
                        scheduler.setupEdge(idInit, calcPos(length, coordX, coordY, coordZ + 1));

                    } else if (coordZ == length - 1) {
                        // last node
                        if (!directed) {
                            scheduler.setupEdge(idInit, calcPos(length, coordX, coordY, coordZ - 1));
                        }

                    } else {
                        // intermediate node
                        scheduler.setupEdge(idInit, calcPos(length, coordX, coordY, coordZ + 1));

                        if (!directed) {
                            scheduler.setupEdge(idInit, calcPos(length, coordX, coordY, coordZ - 1));
                        }
                    }

                }
            }
        }

        // Connect any remaining agents with the first agent
        for (int i = length * length * length; i < scheduler.size(); i++) {
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
     * @param size   of 3D grid.
     * @param coordX the x coordinates of the agent.
     * @param coordY the y coordinates of the agent.
     * @param coordZ the z coordinates of the agent.
     * @return the position of the agent in the array list.
     */
    private int calcPos(final int size, final int coordX, final int coordY, final int coordZ) {
        return coordX + coordY * size + coordZ * size * size;
    }

}
