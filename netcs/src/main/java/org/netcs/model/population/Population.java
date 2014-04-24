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
public class Population<State> {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(Population.class);

    private final SimpleGraph<PopulationNode<State>, DefaultEdge> graph;
    private final Map<DefaultEdge, PopulationLink<State>> edges;

    /**
     * Constructor that initializes the container of agents.
     */
    public Population() {
        graph = new SimpleGraph<>(DefaultEdge.class);
        edges = new HashMap<>();
    }

    /**
     * Constructor that initializes the container of agents to a given initial size.
     *
     * @param size the initial size of the population.
     */
    public Population(final long size) {
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
}
