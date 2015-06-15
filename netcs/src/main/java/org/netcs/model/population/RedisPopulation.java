package org.netcs.model.population;

import org.apache.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.netcs.LookupService;

import java.util.*;

/**
 * Represents the population of agents.
 */
public class RedisPopulation implements Population {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(RedisPopulation.class);

    private final SimpleGraph<PopulationNode, DefaultEdge> graph;
    private final Map<DefaultEdge, PopulationLink> edges;
    private LookupService lookupService;
    protected long experimentId;

    /**
     * Constructor that initializes the container of agents.
     */
    public RedisPopulation(final LookupService lookupService) {
        graph = new SimpleGraph<>(DefaultEdge.class);
        edges = new HashMap<>();
        this.lookupService = lookupService;
    }

    /**
     * Constructor that initializes the container of agents to a given initial size.
     *
     * @param size the initial size of the population.
     */
    public RedisPopulation(final long size, final LookupService lookupService) {
        graph = new SimpleGraph<>(DefaultEdge.class);
        long start = System.currentTimeMillis();
        LOGGER.debug("Creating " + size + " GraphNodes...");
        for (int i = 0; i < size; i++) {
            final String name = "v" + i;
            LOGGER.trace("Creating Node " + name);
            graph.addVertex(new PopulationNode(name));
        }
        LOGGER.debug("Created " + size + " GraphNodes in " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        LOGGER.debug("Creating GraphEdges...");
        for (PopulationNode firstVertex : graph.vertexSet()) {
            for (PopulationNode secondVertex : graph.vertexSet()) {
                if (!secondVertex.equals(firstVertex)) {
                    LOGGER.trace("Creating Edge between " + firstVertex + " and " + secondVertex);
                    graph.addEdge(firstVertex, secondVertex);
                }
            }
        }
        LOGGER.debug("Created GraphEdges in " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        edges = new HashMap<>();
        LOGGER.debug("Creating Population Edges...");
        for (DefaultEdge defaultEdge : graph.edgeSet()) {
            edges.put(defaultEdge, new PopulationLink(defaultEdge));
        }
        LOGGER.debug("Created Edges in " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * The size of the population.
     *
     * @return the number of agents.
     */
    public final int size() {
        return graph.vertexSet().size();
    }

    public Collection<PopulationLink> getEdges() {
        return edges.values();
    }

    public Collection<PopulationNode> getNodes() {
        return graph.vertexSet();
    }

    public PopulationNode getAgent(int index) {
        return (PopulationNode) graph.vertexSet().toArray()[index];
    }

    public PopulationLink getEdge(PopulationNode initiatorPopulationNode, PopulationNode responderPopulationNode) {
        DefaultEdge edge = graph.getEdge(initiatorPopulationNode, responderPopulationNode);
        return edges.get(edge);
    }

    public long getDegree(PopulationNode node) {
        return lookupService.getDegree(experimentId, node.getNodeName());
    }

    public long getActualDegree(PopulationNode node) {
        return getActiveNeighbors(node).size();
    }

    public void initCache(final long experimentId) {
        this.experimentId = experimentId;
        for (final PopulationNode node : getNodes()) {
            lookupService.updateDegree(experimentId, node.getNodeName(), getActualDegree(node));
        }
    }

    @Override
    public void fixCacheDegree(PopulationNode node) {
        lookupService.updateDegree(experimentId, node.getNodeName(), getActualDegree(node));
    }

    @Override
    public Set<PopulationNode> getActiveNeighbors(PopulationNode node) {
        Set<PopulationNode> activeNeighbors = new HashSet<>();
        for (final PopulationNode node1 : getNodes()) {
            if (node.equals(node1)) {
                continue;
            }
            if (getEdge(node, node1).getState().equals("1") ||
                    getEdge(node1, node).getState().equals("1")) {
                activeNeighbors.add(node1);
            }
        }
        return activeNeighbors;
    }
}
