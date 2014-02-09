package ppsim.scheduler.graph.edge;

import ppsim.model.AbstractAgent;
import ppsim.model.AgentIntPair;
import ppsim.model.Population;
import ppsim.model.PopulationProtocol;
import ppsim.scheduler.AbstractScheduler;
import ppsim.scheduler.graph.GraphScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements Abstract Graph Edge Scheduler.
 *
 * @param <State> the variable type for the state of the agent.
 */
abstract class AbstractGraphEdgeScheduler<State>
        extends AbstractScheduler<State>
        implements GraphScheduler {

    /**
     * List with edges connecting agents.
     */
    private ArrayList<AgentIntPair> graph;

    /**
     * Connect Scheduler to specific population and protocol.
     *
     * @param pop  the population object.
     * @param prot the protocol object.
     */
    public void connect(final Population<State> pop, final PopulationProtocol<State> prot) {
        super.connect(pop, prot);

        // Initial AbstractAgent Pairs
        graph = new ArrayList<AgentIntPair>();

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
        graph.add(new AgentIntPair(start, end));
    }

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler uniformly randomly selects the initiator and responder agents from the population.
     */
    public void interact() {
        // Decide random agent pair
        final int pair = randomInt(graph.size());
        final AgentIntPair apair = graph.get(pair);

        // Retrieve initiator and responder agent from the agent pair
        final AbstractAgent<State> initAgent = getAgent(apair.getInitiator());
        final AbstractAgent<State> respAgent = getAgent(apair.getResponder());

        // Conduct interaction for given pair of agents
        interact(initAgent, respAgent);
    }

    public void interact(List<Integer> l1, List<Integer> l2, long round) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
