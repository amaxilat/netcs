package ppsim.scheduler;

import ppsim.model.AbstractAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implements simple state scheduler.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class StateScheduler<State> extends AbstractScheduler<State> {

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

        AbstractAgent<State> initAgent, respAgent;

        // Initialize to the same agent
        initAgent = getAgent(0);
        respAgent = getAgent(0);

        // Make sure initiator and responder are different agents.
        while (initAgent.equals(respAgent)) {

            // Decide state of initiator agent.
            final int init = randomInt(totStates);
            final State initState = states.get(init);
            final List<AbstractAgent<State>> initStateList = getAgentsAtState(initState);
            final int initRnd = randomInt(initStateList.size());
            initAgent = initStateList.get(initRnd);

            // Decide state of responder agent.
            final int resp = randomInt(totStates);
            final State respState = states.get(resp);
            final List<AbstractAgent<State>> respStateList = getAgentsAtState(respState);
            final int respRnd = randomInt(respStateList.size());
            respAgent = respStateList.get(respRnd);
        }

        // Conduct interaction for given pair of agents
        interact(initAgent, respAgent);
    }

}
