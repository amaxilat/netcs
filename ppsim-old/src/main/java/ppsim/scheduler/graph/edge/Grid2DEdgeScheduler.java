package ppsim.scheduler.graph.edge;

import ppsim.scheduler.graph.Grid2DGenerator;

/**
 * Implements scheduler based on a 2D grid connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class Grid2DEdgeScheduler<State> extends AbstractGraphEdgeScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new Grid2DGenerator().generate(this, true);
    }

}
