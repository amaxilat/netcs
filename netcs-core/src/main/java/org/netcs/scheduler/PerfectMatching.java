package org.netcs.scheduler;


import org.apache.log4j.Logger;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Implements simple randomized scheduler.
 */
//TODO: Randomly choose the resulting states
public class PerfectMatching extends AbstractScheduler {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(PerfectMatching.class);

    /**
     * Performs an interaction between an initiator agent and a responder agent. The scheduler uniformly randomly
     * selects the initiator and responder agents from the population.
     */
    public boolean interact(long index) {
        final long start = System.currentTimeMillis();

        List<PopulationNode> shuffledList = new ArrayList<>(population.getNodes());
        Collections.shuffle(shuffledList);
        Iterator<PopulationNode> nodesIterator = shuffledList.iterator();
        boolean result = false;
        while (nodesIterator.hasNext()) {
            final PopulationNode initiatorPopulationNode = nodesIterator.next();
            String initiatorPopulationNodeState = initiatorPopulationNode.getState();
            final PopulationNode responderPopulationNode = nodesIterator.next();
            String responderPopulationNodeState = responderPopulationNode.getState();
            final PopulationLink link = population.getEdge(initiatorPopulationNode, responderPopulationNode);
            final String prevState = link.getState();
            // Conduct interaction for given pair of agents
            result = interact(initiatorPopulationNode, responderPopulationNode, link) || result;
//            if (initiatorPopulationNode.getState().equals("l")) {
//                LOGGER.debug(String.format("[%d] interaction [ %s:%s -- %s:%s ] c1:%d c2:%d", index, initiatorPopulationNode.getNodeName(), initiatorPopulationNodeState, responderPopulationNode.getNodeName(), responderPopulationNodeState, initiatorPopulationNode.getCount1(), initiatorPopulationNode.getCount2()));
//            }
            if (result) {
                final String newState = link.getState();
                if (!prevState.equals(newState)) {
                    population.fixCacheDegree(initiatorPopulationNode);
                    population.fixCacheDegree(responderPopulationNode);
                }
                LOGGER.info(String.format("[%d] interaction [ %s:%s -- %s:%s ]", index, initiatorPopulationNode.getNodeName(), initiatorPopulationNodeState, responderPopulationNode.getNodeName(), responderPopulationNodeState));
            }
            //LOGGER.debug("\t" + link);
        }
        LOGGER.debug("[perfectMatchingScheduler:interact] " + (System.currentTimeMillis() - start) + " ms");
        return result;
    }
}
