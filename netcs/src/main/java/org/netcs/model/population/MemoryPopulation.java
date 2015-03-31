package org.netcs.model.population;

import org.apache.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the population of agents.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class MemoryPopulation<State> implements Population<State> {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(MemoryPopulation.class);

    private final SimpleGraph<PopulationNode<State>, DefaultEdge> graph;
    private final Map<DefaultEdge, PopulationLink<State>> edges;
    protected Map<String, Long> nodesDegree;
    protected long experimentId;

    /**
     * Constructor that initializes the container of agents.
     */
    public MemoryPopulation() {
        graph = new SimpleGraph<>(DefaultEdge.class);
        edges = new HashMap<>();
        nodesDegree = new HashMap<>();
    }

    /**
     * Constructor that initializes the container of agents to a given initial size.
     *
     * @param size the initial size of the population.
     */
    public MemoryPopulation(final long size) {
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
        for (final PopulationNode<State> firstVertex : graph.vertexSet()) {
            for (final PopulationNode<State> secondVertex : graph.vertexSet()) {
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
        for (final DefaultEdge defaultEdge : graph.edgeSet()) {
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
        return nodesDegree.get(node.getNodeName());
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
        if (nodesDegree == null) {
            nodesDegree = new HashMap<>();
        }
        for (final PopulationNode<State> node : getNodes()) {
            nodesDegree.put(node.getNodeName(), getActualDegree(node));
        }
    }

    public void fixCacheDegree(PopulationNode<State> node) {
        nodesDegree.put(node.getNodeName(), getActualDegree(node));
    }
}