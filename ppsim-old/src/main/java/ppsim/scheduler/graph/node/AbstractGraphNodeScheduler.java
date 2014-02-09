package ppsim.scheduler.graph.node;

import ppsim.model.AbstractAgent;
import ppsim.model.Population;
import ppsim.model.PopulationProtocol;
import ppsim.scheduler.AbstractScheduler;
import ppsim.scheduler.graph.GraphScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements Abstract Graph Edge Scheduler.
 *
 * @param <State> the variable type for the state of the agent.
 */
abstract class AbstractGraphNodeScheduler<State>
        extends AbstractScheduler<State>
        implements GraphScheduler {

    /**
     * Map with Lists of edges connecting agents.
     */
    private Map<Integer, ArrayList<Integer>> graph;

    /**
     * Connect Scheduler to specific population and protocol.
     *
     * @param pop  the population object.
     * @param prot the protocol object.
     */
    public void connect(final Population<State> pop, final PopulationProtocol<State> prot) {
        super.connect(pop, prot);

        // Initial AbstractAgent Pairs
        graph = new HashMap<Integer, ArrayList<Integer>>(pop.size());

        // Initialize the connectivity graph
        setupGraph();
    }

    /**
     * Initializes the connectivity graph of the population.
     */
    protected abstract void setupGraph();

    /**
     * Add an edge to the graph.
     *
     * @param start the index of the initiator agent.
     * @param end   the index of the responder agent.
     */
    public void setupEdge(final int start, final int end) {
        if (graph.containsKey(start)) {
            final ArrayList<Integer> edges = graph.get(start);
            edges.add(end);

        } else {
            final ArrayList<Integer> edges = new ArrayList<Integer>();
            edges.add(end);
            graph.put(start, edges);
        }
    }

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler uniformly randomly selects the initiator and responder agents from the population.
     */
    public void interact() {
        // Decide random agent
        final int init = randomAgentID();

        // Pick random neighbor
        final ArrayList<Integer> neighbors = graph.get(init);
        final int respRnd = randomAgentID();
        final int resp = neighbors.get(respRnd);

        // Retrieve initiator and responder agent from the agent pair
        final AbstractAgent<State> initAgent = getAgent(init);
        final AbstractAgent<State> respAgent = getAgent(resp);

        // Conduct interaction for given pair of agents
        interact(initAgent, respAgent);
    }
}
