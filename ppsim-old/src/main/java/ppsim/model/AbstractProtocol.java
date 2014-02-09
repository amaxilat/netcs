package ppsim.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of a population protocol.
 *
 * @param <State> the variable type for the state of the agent.
 */
public abstract class AbstractProtocol<State> implements PopulationProtocol<State> {

    /**
     * Map of state-pairs to state-pairs.
     */
    private final Map<StatePair<State>, StatePair<State>> transitions;

    /**
     * Default constructor.
     */
    public AbstractProtocol() {
        // Initialize transisions map
        transitions = new HashMap<StatePair<State>, StatePair<State>>();

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
    protected final void addEntry(final StatePair<State> fromPair, final StatePair<State> toPair) {
        transitions.put(fromPair, toPair);
    }

    /**
     * Access the protocol state transision map.
     *
     * @return A Map of StatePair to StatePair objects.
     */
    public final Map<StatePair<State>, StatePair<State>> getTransisionMap() {
        return transitions;
    }

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     *
     * @param initiator the initiator of the interaction.
     * @param responder the responder of the interaction.
     */
    public void interact(final AbstractAgent<State> initiator, final AbstractAgent<State> responder) {
        final StatePair<State> startingState = new StatePair<>(initiator.getState(), responder.getState());
        final StatePair<State> newState = transitions.get(startingState);

        // undefined interaction
        if (newState != null) {
            // Change the states of the agents.
            initiator.setState(newState.getInitiatorState());
            responder.setState(newState.getResponderState());
        }
    }

}
