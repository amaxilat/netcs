package org.netcs.model.population;

import org.apache.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

/**
 * Represents the population of agents.
 */
public class SimplePopulation implements Population {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(SimplePopulation.class);


    private final Map<String, PopulationNode> nodes;
    private final Map<String, PopulationLink> edges;
    protected Map<String, Long> nodesDegree;
    protected long experimentId;
    Map<String, Set<PopulationNode>> activeNeighborsCache;

    /**
     * Constructor that initializes the container of agents.
     */
    public SimplePopulation() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
        nodesDegree = new HashMap<>();
        activeNeighborsCache = new HashMap<>();
    }

    /**
     * Constructor that initializes the container of agents to a given initial size.
     *
     * @param size the initial size of the population.
     */
    public SimplePopulation(final long size) {
        long start = System.currentTimeMillis();
        LOGGER.info("Creating " + size + " GraphNodes...");
        nodes = new HashMap<>();
        edges = new HashMap<>();
        for (int i = 0; i < size; i++) {
            final String name = "v" + i;
            LOGGER.trace("Creating Node " + name);
            final PopulationNode newNode = new PopulationNode(name);
            for (final PopulationNode otherNode : nodes.values()) {
                edges.put(otherNode.getNodeName() + "--" + newNode.getNodeName(), new PopulationLink(null));
            }
            nodes.put(name, newNode);
            if ((i * 100) / size % 10 == 0) {
                LOGGER.info("Creating Graph :" + ((i * 100) / size) + "%");
            }
        }
        LOGGER.info("Created " + size + " GraphNodes in " + (System.currentTimeMillis() - start) + "ms");
//
//        start = System.currentTimeMillis();
//        LOGGER.info("Creating GraphEdges...");

//        for (final PopulationNode firstVertex : nodes.values()) {
//            for (final PopulationNode secondVertex : nodes.values()) {
//                if (!secondVertex.equals(firstVertex)) {
//                    LOGGER.trace("Creating Edge between " + firstVertex + " and " + secondVertex);
//                    if (!edges.containsKey(secondVertex.getNodeName() + "--" + firstVertex.getNodeName())) {
//                    }
//                }
//            }
//        }
//        LOGGER.info("Created GraphEdges in " + (System.currentTimeMillis() - start) + "ms");
        activeNeighborsCache = new HashMap<>();
    }

    /**
     * The size of the population.
     *
     * @return the number of agents.
     */
    public final int size() {
        return nodes.keySet().size();
    }

    public Collection<PopulationLink> getEdges() {
        return edges.values();
    }

    public Collection<PopulationNode> getNodes() {
        return nodes.values();
    }

    public PopulationNode getAgent(int index) {
        return (PopulationNode) nodes.values().toArray()[index];
    }

    public PopulationLink getEdge(PopulationNode initiatorPopulationNode, PopulationNode responderPopulationNode) {
        if (edges.containsKey(initiatorPopulationNode.getNodeName() + "--" + responderPopulationNode.getNodeName())) {
            return edges.get(initiatorPopulationNode.getNodeName() + "--" + responderPopulationNode.getNodeName());
        } else if (edges.containsKey(responderPopulationNode.getNodeName() + "--" + initiatorPopulationNode.getNodeName())) {
            return edges.get(responderPopulationNode.getNodeName() + "--" + initiatorPopulationNode.getNodeName());
        } else {
            return null;
        }
    }

    public long getDegree(PopulationNode node) {
        return nodesDegree.get(node.getNodeName());
    }

    public long getActualDegree(PopulationNode node) {
        return getActualActiveNeighbors(node).size();
    }

    public Set<PopulationNode> getActiveNeighbors(final PopulationNode node) {
        if (!activeNeighborsCache.containsKey(node.getNodeName())) {
            return getActualActiveNeighbors(node);
        } else {
            return activeNeighborsCache.get(node.getNodeName());
        }
    }

    public Set<PopulationNode> getActualActiveNeighbors(final PopulationNode node) {
        final Set<PopulationNode> activeNeighbors = new HashSet<>();
        for (final PopulationNode node1 : getNodes()) {
            if (node.equals(node1)) {
                continue;
            }
            if ((getEdge(node, node1) != null && "1".equals(getEdge(node, node1).getState())) ||
                    ((getEdge(node1, node) != null) && "1".equals(getEdge(node1, node).getState()))) {
                activeNeighbors.add(node1);
            }
        }
        activeNeighborsCache.put(node.getNodeName(), activeNeighbors);
        return activeNeighbors;
    }

    public void initCache(final long experimentId) {
        this.experimentId = experimentId;
        if (nodesDegree == null) {
            nodesDegree = new HashMap<>();
        }
        for (final PopulationNode node : getNodes()) {
//            TODO : REMOVE ME
//            nodesDegree.put(node.getNodeName(), getActualDegree(node));
            nodesDegree.put(node.getNodeName(), 0L);
        }
    }

    public void fixCacheDegree(PopulationNode node) {
        nodesDegree.put(node.getNodeName(), getActualDegree(node));
    }

}
