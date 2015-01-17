package org.netcs.model.population;

import org.apache.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.netcs.LookupService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the population of agents.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class RedisPopulation<State> implements Population<State> {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(RedisPopulation.class);

    private final SimpleGraph<PopulationNode<State>, DefaultEdge> graph;
    private final Map<DefaultEdge, PopulationLink<State>> edges;
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
            graph.addVertex(new PopulationNode<State>(name));
        }
        LOGGER.debug("Created " + size + " GraphNodes in " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        LOGGER.debug("Creating GraphEdges...");
        for (PopulationNode<State> firstVertex : graph.vertexSet()) {
            for (PopulationNode<State> secondVertex : graph.vertexSet()) {
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
            edges.put(defaultEdge, new PopulationLink<State>(defaultEdge));
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

    public Collection<PopulationLink<State>> getEdges() {
        return edges.values();
    }

    public Collection<PopulationNode<State>> getNodes() {
        return graph.vertexSet();
    }

    public PopulationNode<State> getAgent(int index) {
        return (PopulationNode<State>) graph.vertexSet().toArray()[index];
    }

    public PopulationLink<State> getEdge(PopulationNode<State> initiatorPopulationNode, PopulationNode<State> responderPopulationNode) {
        DefaultEdge edge = graph.getEdge(initiatorPopulationNode, responderPopulationNode);
        return edges.get(edge);
    }

    public long getDegree(PopulationNode<State> node) {
        return lookupService.getDegree(experimentId, node.getNodeName());
    }

    public long getActualDegree(PopulationNode<State> node) {
        long nodeDegree = 0;
        for (final PopulationNode<State> node1 : getNodes()) {
            if (node.equals(node1)) {
                continue;
            }
            if (getEdge(node, node1).getState().equals("1") ||
                    getEdge(node1, node).getState().equals("1")) {
                nodeDegree++;
            }
        }
        return nodeDegree;
    }

    public void initCache(final long experimentId) {
        this.experimentId = experimentId;
        for (final PopulationNode<State> node : getNodes()) {
            lookupService.updateDegree(experimentId, node.getNodeName(), getActualDegree(node));
        }
    }

    @Override
    public void fixCacheDegree(PopulationNode<State> node) {
        lookupService.updateDegree(experimentId, node.getNodeName(), getActualDegree(node));
    }
}
