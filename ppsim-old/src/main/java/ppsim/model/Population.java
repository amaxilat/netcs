package ppsim.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the population of agents.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class Population<State> {

    /**
     * container of agents.
     */
    private final Map<Integer, AbstractAgent<State>> agents;
    private final Map<String, AbstractAgent<State>> links;


    /**
     * Constructor that initializes the container of agents.
     */
    public Population() {
        agents = new HashMap<>();
        links = new HashMap<>();
    }

    /**
     * Constructor that initializes the container of agents to a given initial size.
     *
     * @param size the initial size of the population.
     */
    public Population(final int size) {
        agents = new HashMap<>(size);
        links = new HashMap<>();
    }

    /**
     * Retrieves an agent from the populetion.
     *
     * @param index the position of the agent in the population list.
     * @return the agent instance.
     */
    public final AbstractAgent<State> getAgent(final int index) {
        return agents.get(index);
    }

    /**
     * Sets an agent to a particular position in the population.
     *
     * @param agent the agent instance.
     */
    public void setAgent(final AbstractAgent<State> agent) {
        agents.put(agent.getAgentID(), agent);
    }

    /**
     * The size of the population.
     *
     * @return the number of agents.
     */
    public final int size() {
        return agents.size();
    }

    /**
     * Adds a new agent to the population.
     *
     * @param agent the instance of the agent to add.
     */
    protected void addAgent(final AbstractAgent<State> agent) {
        agent.setAgentID(size());
        agents.put(agent.getAgentID(), agent);
    }

    /**
     * Removes the given agent from the internal state maps.
     *
     * @param agent the agent to remove.
     */
    protected final void removeFromState(final AbstractAgent<State> agent) {
        final State state = agent.getState();
        if (state == null) {
            return;
        }

        // Check that counter for this state exists
        if (stateAgentsCount.containsKey(state)) {
            // Reduce counter and update Map
            final long count = stateAgentsCount.get(state) - 1;
            stateAgentsCount.put(state, count);

        } else {
            stateAgentsCount.put(state, (long) 0);

        }

        // Check that list for this state exists
        if (stateAgents.containsKey(state)) {
            // Remove agent from list
            final ArrayList<AbstractAgent<State>> agentList = stateAgents.get(state);
            agentList.remove(agent);

        } else {
            stateAgents.put(state, new ArrayList<AbstractAgent<State>>());

        }
    }

    /**
     * Adds the given agent from the internal state maps.
     *
     * @param agent the agent to remove.
     */
    protected final void addToState(final AbstractAgent<State> agent) {
        final State state = agent.getState();
        if (state == null) {
            return;
        }

        // Check that counter for this state exists
        if (!stateAgentsCount.containsKey(state)) {
            stateAgentsCount.put(state, (long) 0);
        }

        // Increase counter and update Map
        final long count = stateAgentsCount.get(state) + 1;
        stateAgentsCount.put(state, count);

        // Check that list for this state exists
        if (!stateAgents.containsKey(state)) {
            stateAgents.put(state, new ArrayList<AbstractAgent<State>>());
        }

        // Add agent to list
        final ArrayList<AbstractAgent<State>> agentList = stateAgents.get(state);
        agentList.add(agent);
    }

    /**
     * Returns a Set with the states of the protocol.
     *
     * @return a Set of protocol States.
     */
    public final Set<State> getProtocolStates() {
        return stateAgents.keySet();
    }

    /**
     * Returns an iterator to the collection of agents.
     *
     * @return an iterator the collection of agents.
     */
    public final Iterator<AbstractAgent<State>> getAgentsIterator() {
        return agents.values().iterator();
    }

    /**
     * Returns a List of agents at the given protocol state.
     *
     * @param state the protocol state.
     * @return a list of agents.
     */
    public final List<AbstractAgent<State>> getAgentsAtState(final State state) {
        return stateAgents.get(state);
    }

    /**
     * Returns a Set that of protocol states and the count of agents under this state.
     *
     * @return a Set with (State,Long) entries.
     */
    public final Set<Map.Entry<State, Long>> getStateCount() {
        return stateAgentsCount.entrySet();
    }

    /**
     * Returns the number of agents in the given protocol state.
     *
     * @param state the protocol state.
     * @return the count of agent in this state.
     */
    public final long getStateCount(final State state) {
        if (!stateAgentsCount.containsKey(state)) {
            return 0;
        }
        return stateAgentsCount.get(state);
    }

}
