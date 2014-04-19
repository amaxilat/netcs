package org.ppsim.scheduler;


import org.apache.log4j.Logger;
import org.ppsim.model.population.PopulationLink;
import org.ppsim.model.population.PopulationNode;

/**
 * Implements simple randomized scheduler.
 *
 * @param <State> the variable type for the state of the agent.
 */
//TODO: Randomly choose the resulting states
public class RandomScheduler<State> extends AbstractScheduler<State> {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(RandomScheduler.class);

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler uniformly randomly selects the initiator and responder agents from the population.
     */
    public boolean interact() {
        final int initiator = randomNodeId();
        int responder = randomNodeId();

        // Make sure initiator and responder are different agents.
        while (initiator == responder) {
            responder = randomNodeId();
        }

        final PopulationNode<State> initiatorPopulationNode = getNode(initiator);
        final PopulationNode<State> responderPopulationNode = getNode(responder);
        final PopulationLink<State> link = population.getEdge(initiatorPopulationNode, responderPopulationNode);
        LOGGER.debug(link);
        // Conduct interaction for given pair of agents
        return interact(initiatorPopulationNode, responderPopulationNode, link);
        //LOGGER.debug("\t" + link);
    }

}
