package org.ppsim.scheduler;


import org.ppsim.model.population.PopulationLink;
import org.ppsim.model.population.PopulationNode;

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
        final int initiator = randomNodeId();
        int responder = randomNodeId();

        // Make sure initiator and responder are different agents.
        while (initiator == responder) {
            responder = randomNodeId();
        }

        final PopulationNode<State> inititiatorPopulationNode = getNode(initiator);
        final PopulationNode<State> responderPopulationNode = getNode(responder);
        final PopulationLink<State> link = population.getEdge(inititiatorPopulationNode, responderPopulationNode);

        // Conduct interaction for given pair of agents
        interact(inititiatorPopulationNode, responderPopulationNode, link);
    }

}
