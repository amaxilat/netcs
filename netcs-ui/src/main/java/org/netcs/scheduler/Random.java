package org.netcs.scheduler;


import org.apache.log4j.Logger;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;

/**
 * Implements simple randomized scheduler.
 */
//TODO: Randomly choose the resulting states
public class Random extends AbstractScheduler {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(Random.class);

    /**
     * Performs an interaction between an initiator agent and a responder agent. The scheduler uniformly randomly
     * selects the initiator and responder agents from the population.
     */
    public boolean interact(long index) {
        final long start = System.currentTimeMillis();
        final int initiator = randomNodeId();
        int responder = randomNodeId();

        // Make sure initiator and responder are different agents.
        while (initiator == responder) {
            responder = randomNodeId();
        }


        final PopulationNode initiatorPopulationNode = getNode(initiator);
        String initiatorPopulationNodeState = initiatorPopulationNode.getState();
        final PopulationNode responderPopulationNode = getNode(responder);
        String responderPopulationNodeState = responderPopulationNode.getState();
        final PopulationLink link = population.getEdge(initiatorPopulationNode, responderPopulationNode);
        final String prevState = link.getState();
        LOGGER.debug(link);
        // Conduct interaction for given pair of agents
        final boolean result = interact(initiatorPopulationNode, responderPopulationNode, link);
        if (initiatorPopulationNode.getState().equals("l")) {
            LOGGER.debug(String.format("[%d] interaction [ %s:%s -- %s:%s ] c1:%d c2:%d", index, initiatorPopulationNode.getNodeName(), initiatorPopulationNodeState, responderPopulationNode.getNodeName(), responderPopulationNodeState, initiatorPopulationNode.getCount1(), initiatorPopulationNode.getCount2()));
        }
        if (result) {
            final String newState = link.getState();
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
