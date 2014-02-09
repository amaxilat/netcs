package ppsim.scheduler.graph.edge;

import ppsim.scheduler.graph.LollipopGenerator;

/**
 * Implements scheduler based on a Lolipop connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class LollipopEdgeScheduler<State> extends AbstractGraphEdgeScheduler<State> {

    /**
     * The m parameter of the lollipop graph.
     */
    private final int mParam;

    /**
     * Constructor for specifying m parameter of lollipop graph.
     *
     * @param param -- the m specification of the lollipop graph.
     */
    public LollipopEdgeScheduler(final int param) {
        super();
        mParam = param;
    }

    /**
     * Default constructor.
     */
    public LollipopEdgeScheduler() {
        super();
        mParam = 0;
    }

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new LollipopGenerator().generate(this, true, mParam);
    }

}
