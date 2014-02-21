package org.ppsim.model;

import org.ppsim.model.population.PopulationLink;
import org.ppsim.model.population.PopulationNode;

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

    /**
     * Default constructor.
     */
    public AbstractProtocol() {
        // Initialize transisions map
        transitions = new HashMap<>();

        // Setup entries.
        setupTransisionsMap();
    }

    /**
     * Implemented by subclasses to define the entries of the transision map.
     */
    protected abstract void setupTransisionsMap();

    /**
     * Adds an entry to the transision map.
     *
     * @param fromPair the starting pair of states.
     * @param toPair   the ending pair of states.
     */
    protected final void addEntry(final StateTriple<State> fromPair, final StateTriple<State> toPair) {
        transitions.put(fromPair, toPair);
    }

    /**
     * Access the protocol state transision map.
     *
     * @return A Map of StateTriple to StateTriple objects.
     */
    public final Map<StateTriple<State>, StateTriple<State>> getTransisionMap() {
        return transitions;
    }

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     *
     * @param initiator the initiator of the interaction.
     * @param responder the responder of the interaction.
     */
    public void interact(final PopulationNode<State> initiator, final PopulationNode<State> responder, final PopulationLink<State> link) {
        final StateTriple<State> startingState = new StateTriple<>(initiator.getState(), responder.getState(), link.getState());
        final StateTriple<State> newState = transitions.get(startingState);

        // undefined interaction
        if (newState != null) {
            // Change the states of the agents.
            initiator.setState(newState.getInitiatorState());
            responder.setState(newState.getResponderState());
            link.setState(newState.getLinkState());
        }
    }

}
