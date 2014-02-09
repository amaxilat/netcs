package ppsim.scheduler.graph.node;

import ppsim.scheduler.graph.Grid3DGenerator;

/**
 * Implements scheduler based on a 3D grid connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class Grid3DNodeScheduler<State> extends AbstractGraphNodeScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new Grid3DGenerator().generate(this, false);
    }

}
