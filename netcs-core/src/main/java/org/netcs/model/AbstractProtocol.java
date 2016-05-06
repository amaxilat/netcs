package org.netcs.model;

import org.apache.log4j.Logger;
import org.netcs.config.ConfigFile;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;
import org.netcs.scheduler.PerfectMatching;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of a population protocol.
 */
public abstract class AbstractProtocol {

    private static final Logger LOGGER = Logger.getLogger(AbstractProtocol.class);

    /**
     * Map of state-pairs to state-pairs.
     */
    private final Map<StateTriple, StateTriple> transitions;
    public ConfigFile configFile;
    private StateTriple defaultState = new StateTriple("q", "q", "0");

    /**
     * Default constructor.
     */
    public AbstractProtocol() {
        // Initialize transitions map
        transitions = new HashMap<>();

    }

    /**
     * Implemented by subclasses to define the entries of the transition map.
     */
    protected abstract void setupTransitionsMap();

    /**
     * Adds an entry to the transition map.
     *
     * @param fromPair the starting pair of states.
     * @param toPair   the ending pair of states.
     */
    protected final void addEntry(final StateTriple fromPair, final StateTriple toPair) {
        transitions.put(fromPair, toPair);
    }

    /**
     * Access the protocol state transition map.
     *
     * @return A Map of StateTriple to StateTriple objects.
     */
    public final Map<StateTriple, StateTriple> getTransitionMap() {
        return transitions;
    }

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     *
     * @param initiator the initiator of the interaction.
     * @param responder the responder of the interaction.
     */
    public boolean interact(final PopulationNode initiator, final PopulationNode responder, final PopulationLink link) {
        final StateTriple startingState = new StateTriple(initiator.getState(), responder.getState(), link.getState());
        if (initiator.getState().equals("l") && responder.getState().equals("q0")) {
            initiator.incCount1();
        } else if (initiator.getState().equals("l") && responder.getState().equals("q1")) {
            initiator.incCount2();
        }
        StateTriple newState = transitions.get(startingState);
        // undefined interaction
        if (newState == null) {
            //TODO: remove this when done
            newState = defaultState;
        }
        if (newState != null) {
            // Change the states of the agents.
            LOGGER.info("interaction [ " + startingState + "-->" + newState + "]");
            initiator.setState(newState.getInitiatorState());
            responder.setState(newState.getResponderState());
            link.setState(newState.getLinkState());
            return true;
        }
        return false;
    }
}
