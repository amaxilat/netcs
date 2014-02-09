package ppsim.scheduler.graph;

/**
 * Common methods implemented by all restricted population protocols.
 */
public interface GraphScheduler {

    /**
     * Add an edge to the graph.
     *
     * @param start the index of the initiator agent.
     * @param end   the index of the responder agent.
     */
    void setupEdge(final int start, final int end);

    /**
     * The size of the graph.
     *
     * @return the number of nodes.
     */
    int size();

}
