package org.netcs.model.population;

import java.util.Collection;

/**
 * Represents the population of agents.
 */
public interface Population {


    /**
     * The size of the population.
     *
     * @return the number of agents.
     */
    public int size();

    public Collection<PopulationLink> getEdges();

    public Collection<PopulationNode> getNodes();

    public PopulationNode getAgent(int index);

    public PopulationLink getEdge(PopulationNode initiatorPopulationNode, PopulationNode responderPopulationNode);

    public long getDegree(PopulationNode node);

    public long getActualDegree(PopulationNode node);

    public void initCache(final long experimentId);

    public void fixCacheDegree(PopulationNode node);
}
