package ppsim.scheduler.graph.state;

import ppsim.scheduler.graph.GnpGenerator;

/**
 * Implements scheduler based on a G<sub>n,p</sub> connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class GnpStateScheduler<State> extends AbstractGraphStateScheduler<State> {

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new GnpGenerator().generate(this, false);
    }

}
