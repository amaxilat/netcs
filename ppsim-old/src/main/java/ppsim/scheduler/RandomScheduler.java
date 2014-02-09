package ppsim.scheduler;

import ppsim.model.AbstractAgent;

/**
 * Implements simple randomized scheduler.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class RandomScheduler<State> extends AbstractScheduler<State> {

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler uniformly randomly selects the initiator and responder agents from the population.
     */
    public void interact() {
        final int init = randomAgentID();
        int resp = randomAgentID();

        // Make sure initiator and responder are different agents.
        while (init == resp) {
            resp = randomAgentID();
        }

        final AbstractAgent<State> initAgent = getAgent(init);
        final AbstractAgent<State> respAgent = getAgent(resp);

        // Conduct interaction for given pair of agents
        interact(initAgent, respAgent);
    }

}
