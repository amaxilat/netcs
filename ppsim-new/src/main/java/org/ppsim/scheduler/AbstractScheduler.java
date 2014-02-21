package org.ppsim.scheduler;

import org.ppsim.model.AbstractProtocol;
import org.ppsim.model.Scheduler;
import org.ppsim.model.population.Population;
import org.ppsim.model.population.PopulationLink;
import org.ppsim.model.population.PopulationNode;

import java.util.Random;

/**
 * Implements an abstract scheduler.
 *
 * @param <State> the variable type for the state of the agent.
 */
public abstract class AbstractScheduler<State> implements Scheduler<State> {

    /**
     * Random number generator.
     */
    private final Random randomGenerator;

    /**
     * Population holder.
     */
    protected Population<State> population;

    /**
     * Actual protocol.
     */
    private AbstractProtocol<State> protocol;

    /**
     * Default constructor.
     */
    public AbstractScheduler() {
        randomGenerator = new Random();
    }

    /**
     * Connect Scheduler to specific population and protocol.
     *
     * @param population the population object.
     * @param protocol   the protocol object.
     */
    public void connect(final Population<State> population, final AbstractProtocol<State> protocol) {
        this.population = population;
        this.protocol = protocol;
    }

    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler uniformly randomly selects the initiator and responder agents from the population.
     */
    public abstract void interact();

    /**
     * The size of the population.
     *
     * @return the number of agents.
     */
    public final int size() {
        return population.size();
    }

    /**
     * Randomly picks an integer from the range 0...(population size).
     *
     * @return a random integer
     */
    protected final int randomNodeId() {
        return randomGenerator.nextInt(size());
    }

    /**
     * Retrieves an agent from the Population.
     *
     * @param index the position of the agent in the population list.
     * @return the agent instance.
     */
    protected final PopulationNode<State> getNode(final int index) {
        return population.getAgent(index);
    }


    /**
     * Invokes the Protocol to perform an interaction between an initiator agent and a responder agent.
     *
     * @param initiator the initiator of the interaction.
     * @param responder the responder of the interaction.
     */
    protected void interact(final PopulationNode<State> initiator, final PopulationNode<State> responder, final PopulationLink<State> link) {
        protocol.interact(initiator, responder, link);
    }

}
