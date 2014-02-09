package ppsim.protocols.am;

import ppsim.model.AbstractAgent;
import ppsim.model.Population;

/**
 * Implements the agent of the Approximate Majority protocol.
 */
public class AMAgent extends AbstractAgent<AMState> {

    /**
     * Default constructor.
     *
     * @param pop the Population holder.
     */
    public AMAgent(final Population<AMState> pop) {
        super(pop);
    }

    /**
     * Initializes the state of the agent.
     */
    public void initAgent() {
        setState(AMState.STATE_B);
    }

}
