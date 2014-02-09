package ppsim.scheduler.graph.node;

import ppsim.scheduler.graph.GnpGenerator;

/**
 * Implements scheduler based on a G<sub>n,p</sub> connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class GnpNodeScheduler<State> extends AbstractGraphNodeScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new GnpGenerator().generate(this, false);
    }

}
