package org.netcs.scheduler;


import org.apache.log4j.Logger;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;

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
    private static final Logger LOGGER = Logger.getLogger(RandomScheduler.class);

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler uniformly randomly selects the initiator and responder agents from the population.
     *
     * @param index
     */
    public boolean interact(long index) {
        final long start = System.currentTimeMillis();
        final int initiator = randomNodeId();
        int responder = randomNodeId();

        // Make sure initiator and responder are different agents.
        while (initiator == responder) {
            responder = randomNodeId();
        }


        final PopulationNode<State> initiatorPopulationNode = getNode(initiator);
        State initiatorPopulationNodeState = initiatorPopulationNode.getState();
        final PopulationNode<State> responderPopulationNode = getNode(responder);
        State responderPopulationNodeState = responderPopulationNode.getState();
        final PopulationLink<State> link = population.getEdge(initiatorPopulationNode, responderPopulationNode);
        final State prevState = link.getState();
        LOGGER.debug(link);
        // Conduct interaction for given pair of agents
        final boolean result = interact(initiatorPopulationNode, responderPopulationNode, link);
        if (initiatorPopulationNode.getState().equals("l")) {
            LOGGER.debug(String.format("[%d] interaction [ %s:%s -- %s:%s ] c1:%d c2:%d", index, initiatorPopulationNode.getNodeName(), initiatorPopulationNodeState, responderPopulationNode.getNodeName(), responderPopulationNodeState, initiatorPopulationNode.getCount1(), initiatorPopulationNode.getCount2()));
        }
        if (result) {
            final State newState = link.getState();
            if (!prevState.equals(newState)) {
                population.fixCacheDegree(initiatorPopulationNode);
                population.fixCacheDegree(responderPopulationNode);
            }

            LOGGER.debug(String.format("[%d] interaction [ %s:%s -- %s:%s ]", index, initiatorPopulationNode.getNodeName(), initiatorPopulationNodeState, responderPopulationNode.getNodeName(), responderPopulationNodeState));
            LOGGER.debug("[randomScheduler:interact] " + (System.currentTimeMillis() - start) + " ms");
        }
        return result;
        //LOGGER.debug("\t" + link);
    }

}
