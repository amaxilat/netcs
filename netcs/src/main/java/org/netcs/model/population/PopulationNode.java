package org.netcs.model.population;

/**
 * Describes a population node.
 */
public class PopulationNode<State> {
    /**
     * The name of the node.
     */
    private final String nodeName;
    private State state;

    /**
     * Name Constructor.
     *
     * @param nodeName the name of the node.
     */
    public PopulationNode(final String nodeName) {
        this.nodeName = nodeName;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PopulationNode{" + nodeName + ":" + state + "}";
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

    public String getNodeName() {
        return nodeName;
    }
}
