package org.netcs.model.population;

/**
 * Describes a population node.
 */
public class PopulationNode {
    /**
     * The name of the node.
     */
    private final String nodeName;
    private String state;
    private int count1;
    private int count2;

    /**
     * Name Constructor.
     *
     * @param nodeName the name of the node.
     */
    public PopulationNode(final String nodeName) {
        this.nodeName = nodeName;
        this.count1 = 0;
        this.count2 = 0;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
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

        return nodeName.equals(that.getNodeName());

    }

    @Override
    public int hashCode() {
        return nodeName.hashCode();
    }

    public String getNodeName() {
        return nodeName;
    }

    public void incCount1() {
        count1++;
    }

    public void incCount2() {
        count2++;
    }

    public int getCount1() {
        return count1;
    }

    public int getCount2() {
        return count2;
    }
}
