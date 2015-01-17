package org.netcs.model.population;

import java.util.Collection;

/**
 * Represents the population of agents.
 *
 * @param <State> the variable type for the state of the agent.
 */
public interface Population<State> {


    /**
     * The size of the population.
     *
     * @return the number of agents.
     */
    public int size();

    public Collection<PopulationLink<State>> getEdges();

    public Collection<PopulationNode<State>> getNodes();

    public PopulationNode<State> getAgent(int index);

    public PopulationLink<State> getEdge(PopulationNode<State> initiatorPopulationNode, PopulationNode<State> responderPopulationNode);

    public long getDegree(PopulationNode<State> node);

    public long getActualDegree(PopulationNode<State> node);

    public void initCache(final long experimentId);

    public void fixCacheDegree(PopulationNode<State> node);
}
