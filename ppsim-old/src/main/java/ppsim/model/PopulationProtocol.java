package ppsim.model;

/**
 * Describes a population protocol.
 *
 * @param <State> the variable type for the state of the agent.
 */
public interface PopulationProtocol<State> {

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     *
     * @param initiator the initiator of the interaction.
     * @param responder the responder of the interaction.
     */
    void interact(AbstractAgent<State> initiator, AbstractAgent<State> responder);

}
