package org.netcs.model;

import org.netcs.config.ConfigFile;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of a population protocol.
 *
 * @param <State> the variable type for the state of the agent.
 */
public abstract class AbstractProtocol<State> {

    /**
     * Map of state-pairs to state-pairs.
     */
    private final Map<StateTriple<State>, StateTriple<State>> transitions;
    public ConfigFile configFile;

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
    protected final void addEntry(final StateTriple<State> fromPair, final StateTriple<State> toPair) {
        transitions.put(fromPair, toPair);
    }

    /**
     * Access the protocol state transition map.
     *
     * @return A Map of StateTriple to StateTriple objects.
     */
    public final Map<StateTriple<State>, StateTriple<State>> getTransitionMap() {
        return transitions;
    }

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     *
     * @param initiator the initiator of the interaction.
     * @param responder the responder of the interaction.
     */
    public boolean interact(final PopulationNode<State> initiator, final PopulationNode<State> responder, final PopulationLink<State> link) {
        final StateTriple<State> startingState = new StateTriple<>(initiator.getState(), responder.getState(), link.getState());
        if (initiator.getState().equals("l") && responder.getState().equals("q0")) {
            initiator.incCount1();
        } else if (initiator.getState().equals("l") && responder.getState().equals("q1")) {
            initiator.incCount2();
        }
        final StateTriple<State> newState = transitions.get(startingState);
        // undefined interaction
        if (newState != null) {
            // Change the states of the agents.
            initiator.setState(newState.getInitiatorState());
            responder.setState(newState.getResponderState());
            link.setState(newState.getLinkState());
            return true;
        }
        return false;
    }
}
