package ppsim.scheduler;

import ppsim.model.AbstractAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implements state scheduler where the initiator and responder must be of different states.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class DiffStateScheduler<State> extends AbstractScheduler<State> {

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

        // Decide state of initiator and responder agents.
        final int init = randomInt(totStates);
        int resp = randomInt(totStates);

        // Make sure initiator and responder are fromdifferent states.
        while (init == resp) {
            resp = randomInt(totStates);
        }

        // Pick initiator agent from given state.
        final State initState = states.get(init);
        final List<AbstractAgent<State>> initStateList = getAgentsAtState(initState);
        final int initRnd = randomInt(initStateList.size());
        final AbstractAgent<State> initAgent = initStateList.get(initRnd);

        // Pick responder agent from given state.
        final State respState = states.get(resp);
        final List<AbstractAgent<State>> respStateList = getAgentsAtState(respState);
        final int respRnd = randomInt(respStateList.size());
        final AbstractAgent<State> respAgent = respStateList.get(respRnd);

        // Conduct interaction for given pair of agents
        interact(initAgent, respAgent);
    }

}
