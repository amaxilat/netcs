package ppsim.scheduler.graph.state;

import ppsim.scheduler.graph.LollipopGenerator;

/**
 * Implements scheduler based on a Lolipop connectivity graph.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class LollipopStateScheduler<State> extends AbstractGraphStateScheduler<State> {

    /**
     * The m parameter of the lollipop graph.
     */
    private final int mParam;

    /**
     * Constructor for specifying m parameter of lollipop graph.
     *
     * @param param -- the m specification of the lollipop graph.
     */
    public LollipopStateScheduler(final int param) {
        super();
        mParam = param;
    }

    /**
     * Default constructor.
     */
    public LollipopStateScheduler() {
        super();
        mParam = 0;
    }

    /**
     * Initializes the connectivity graph of the population.
     */
    public void setupGraph() {
        new LollipopGenerator().generate(this, false, mParam);
    }

}
