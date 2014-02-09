package org.ppsim.model.population;

/**
 * Describes a population node.
 */
public class PopulationNode<State> {
    /**
     * The name of the node.
     */
    private final String nodeName;

    /**
     * Name Constructor.
     *
     * @param nodeName the name of the node.
     */
    public PopulationNode(final String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String toString() {
        return "PopulationNode{" + nodeName + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PopulationNode that = (PopulationNode) o;

        if (!nodeName.equals(that.nodeName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nodeName.hashCode();
    }
}
