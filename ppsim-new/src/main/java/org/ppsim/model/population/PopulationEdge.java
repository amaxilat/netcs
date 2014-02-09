package org.ppsim.model.population;

import org.jgrapht.graph.DefaultEdge;

/**
 * Created with IntelliJ IDEA.
 * User: amaxilatis
 * Date: 2/9/14
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class PopulationEdge<State> extends DefaultEdge {
    private final DefaultEdge defaultEdge;

    public PopulationEdge(final DefaultEdge defaultEdge) {
        this.defaultEdge = defaultEdge;
    }

    @Override
    public String toString() {
        return "PopulationEdge{" + defaultEdge + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PopulationEdge that = (PopulationEdge) o;

        if (!defaultEdge.equals(that.defaultEdge)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return defaultEdge.hashCode();
    }
}
