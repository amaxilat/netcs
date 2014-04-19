package org.ppsim.model.population;

import org.jgrapht.graph.DefaultEdge;

/**
 * Describes a population link.
 */
public class PopulationLink<State> extends DefaultEdge {
    private final DefaultEdge defaultEdge;
    private State state;

    public PopulationLink(final DefaultEdge defaultEdge) {
        this.defaultEdge = defaultEdge;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public DefaultEdge getDefaultEdge() {
        return defaultEdge;
    }

    @Override
    public String toString() {
        return "PopulationLink{" + defaultEdge + ":" + state + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PopulationLink that = (PopulationLink) o;

        if (!defaultEdge.equals(that.defaultEdge)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return defaultEdge.hashCode();
    }
}
