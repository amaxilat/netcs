package org.netcs.model;

/**
 * Describes a population scheduler.
 *
 * @param <State> the variable type for the state of the agent.
 */
public interface Scheduler<State> {

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler defines how the initiator and responder agent are selected from the population.
     */
    boolean interact();


}
