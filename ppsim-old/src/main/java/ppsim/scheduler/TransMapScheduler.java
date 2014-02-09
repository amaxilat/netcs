package ppsim.scheduler;

import ppsim.model.AbstractAgent;
import ppsim.model.Population;
import ppsim.model.PopulationProtocol;
import ppsim.model.StatePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implements scheduler based on the transision matrix of the protocol.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class TransMapScheduler<State> extends AbstractScheduler<State> {

    /**
     * Vector with states of population protocol.
     */
    private ArrayList<StatePair<State>> spairs;

    /**
     * Connect Scheduler to specific population and protocol.
     *
     * @param pop  the population object.
     * @param prot the protocol object.
     */
    public void connect(final Population<State> pop, final PopulationProtocol<State> prot) {
        super.connect(pop, prot);

        // Access transision matrix
        final Map<StatePair<State>, StatePair<State>> transisionsMap =
                getTransisionMap();

        // Initial State Pairs
        spairs = new ArrayList<StatePair<State>>(transisionsMap.keySet());
    }


    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler uniformly randomly selects the initiator and responder agents from the population.
     */
    public void interact() {
        AbstractAgent<State> initAgent, respAgent;

        // Initialze to the same agent
        initAgent = getAgent(0);
        respAgent = getAgent(0);

        // Make sure initiator and responder are different agents.
        while (initAgent.equals(respAgent)) {

            List<AbstractAgent<State>> initStateList = null;
            List<AbstractAgent<State>> respStateList = null;

            while (initStateList == null
                    || respStateList == null
                    || initStateList.isEmpty()
                    || respStateList.isEmpty()) {

                // Decide random state pair
                final int pair = randomInt(spairs.size());
                final StatePair<State> spair = spairs.get(pair);

                // Determine state of initiator agent.
                initStateList = getAgentsAtState(spair.getInitiatorState());

                // Determine state of responder agent.
                respStateList = getAgentsAtState(spair.getResponderState());
            }

            // Pick random initiator
            final int initRnd = randomInt(initStateList.size());
            initAgent = initStateList.get(initRnd);

            // Pick random responder
            final int respRnd = randomInt(respStateList.size());
            respAgent = respStateList.get(respRnd);
        }

        // Conduct interaction for given pair of agents
        interact(initAgent, respAgent);
    }

}
