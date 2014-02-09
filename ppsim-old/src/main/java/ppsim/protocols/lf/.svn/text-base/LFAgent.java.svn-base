package ppsim.protocols.lf;

import ppsim.model.AbstractAgent;
import ppsim.model.Population;

/**
 * Implements the agent of the Leader-Follower protocol.
 */
public class LFAgent extends AbstractAgent<LFState> {

    /**
     * Default constructor.
     *
     * @param pop the Population holder.
     */
    public LFAgent(final Population<LFState> pop) {
        super(pop);
    }

    /**
     * Initializes the state of the agent.
     */
    public void initAgent() {
        setState(LFState.STATE_L);
    }

}
