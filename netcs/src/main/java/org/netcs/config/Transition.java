package org.netcs.config;

/**
 * Describes an entry for the transition map of the algorithm
 */
public class Transition {
    private final String node1;
    private final String node2;
    private final String node1New;
    private final String node2New;
    private final String link;
    private final String linkNew;

    /**
     * Create An entry for the transition map of the algorithm.
     *
     * @param node1    the state of the first node of the interaction
     * @param node2    the state of the second node of the interaction
     * @param link     the state of the link between the nodes of the interaction
     * @param node1New the resulting state of the first node of the interaction
     * @param node2New the resulting state of the second node of the interaction
     * @param linkNew  the resulting state of the link between the nodes of the interaction
     */
    public Transition(final String node1, final String node2, final String link,
                      final String node1New, final String node2New, final String linkNew) {
        this.node1 = node1;
        this.node2 = node2;
        this.node1New = node1New;
        this.node2New = node2New;
        this.link = link;
        this.linkNew = linkNew;
    }

    public String getNode1() {
        return node1;
    }

    public String getNode2() {
        return node2;
    }

    public String getNode1New() {
        return node1New;
    }

    public String getNode2New() {
        return node2New;
    }

    public String getLink() {
        return link;
    }

    public String getLinkNew() {
        return linkNew;
    }

    @Override
    public String toString() {
        return String.format("Transition{%s-[%s]-%s>>%s-[%s]-%s}", node1, link, node2, node1New, linkNew, node2New);
    }
}
