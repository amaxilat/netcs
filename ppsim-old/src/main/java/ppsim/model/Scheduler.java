package ppsim.model;

import java.util.List;

/**
 * Describes a population scheduler.
 *
 * @param <State> the variable type for the state of the agent.
 */
public interface Scheduler<State> {

    /**
     * Connect Scheduler to specific population and protocol.
     *
     * @param pop  the population object.
     * @param prot the protocol object.
     */
    void connect(final Population<State> pop, final PopulationProtocol<State> prot);

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler defines how the initiator and responder agent are selected from the population.
     */
    void interact();

   /*
    * Only for dataset scheduler
    */
    public void interact(List<Integer> l1, List<Integer> l2, long round);

}
