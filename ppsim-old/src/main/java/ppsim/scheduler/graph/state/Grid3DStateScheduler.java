package ppsim.scheduler.graph.state;

import ppsim.scheduler.graph.Grid3DGenerator;

/**
 * Implements scheduler based on a 3D grid connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class Grid3DStateScheduler<State> extends AbstractGraphStateScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new Grid3DGenerator().generate(this, false);
    }

}
