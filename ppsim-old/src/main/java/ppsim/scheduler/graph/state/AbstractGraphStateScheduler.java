package ppsim.scheduler.graph.state;

import ppsim.model.AbstractAgent;
import ppsim.model.Population;
import ppsim.model.PopulationProtocol;
import ppsim.scheduler.AbstractScheduler;
import ppsim.scheduler.graph.GraphScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implements Abstract Graph State Scheduler.
 *
 * @param <State> the variable type for the state of the agent.
 */
abstract class AbstractGraphStateScheduler<State>
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
        // Identify states with atleast 1 agent
        final ArrayList<State> states = new ArrayList<State>();
        final Set<Map.Entry<State, Long>> allStates = getStateCount();
        for (Map.Entry<State, Long> state : allStates) {
            if (state.getValue() > 0) {
                states.add(state.getKey());
            }
        }

        // Actual number of states
        final int totStates = states.size();

        // Decide state of initiator agent.
        final int init = randomInt(totStates);
        final State initState = states.get(init);
        final List<AbstractAgent<State>> initStateList = getAgentsAtState(initState);
        final int initRnd = randomInt(initStateList.size());
        final AbstractAgent<State> initAgent = initStateList.get(initRnd);

        // Pick random neighbor
        final ArrayList<Integer> neighbors = graph.get(initRnd);
        final int respRnd = randomInt(neighbors.size());
        final int resp = neighbors.get(respRnd);
        final AbstractAgent<State> respAgent = getAgent(resp);

        // Conduct interaction for given pair of agents
        interact(initAgent, respAgent);
    }
}
