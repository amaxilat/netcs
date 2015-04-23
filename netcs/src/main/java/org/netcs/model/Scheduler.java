package org.netcs.model;

/**
 * Describes a population scheduler.
 *
 */
public interface Scheduler {

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler defines how the initiator and responder agent are selected from the population.
     * @param index
     */
    boolean interact(long index);


}
