package org.netcs.model.population;

import org.jgrapht.graph.DefaultEdge;

/**
 * Describes a population link.
 */
public class PopulationLink extends DefaultEdge {
    private final DefaultEdge defaultEdge;
    private String state;

    public PopulationLink(final DefaultEdge defaultEdge) {
        this.defaultEdge = defaultEdge;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
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

        return defaultEdge.equals(that.defaultEdge);
    }

    @Override
    public int hashCode() {
        return defaultEdge.hashCode();
    }
}
