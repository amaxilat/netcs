package ppsim.scheduler;

import ppsim.model.AbstractAgent;
import ppsim.model.AbstractProtocol;
import ppsim.model.Population;
import ppsim.model.PopulationProtocol;
import ppsim.model.Scheduler;
import ppsim.model.StatePair;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
    private Population<State> population;

    /**
     * Actual protocol.
     */
    private PopulationProtocol<State> protocol;

    /**
     * Default constructor.
     */
    public AbstractScheduler() {
        randomGenerator = new Random();
    }

    /**
     * Connect Scheduler to specific population and protocol.
     *
     * @param pop  the population object.
     * @param prot the protocol object.
     */
    public void connect(final Population<State> pop, final PopulationProtocol<State> prot) {
        population = pop;        
        protocol = prot;
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
    protected final int randomAgentID() {
        return randomGenerator.nextInt(size());
    }

    /**
     * Randomly picks an integer from the range 0...(maxRange).
     *
     * @param maxRange the maximum range for selecting a random ID
     * @return a random integer
     */
    protected final int randomInt(final int maxRange) {
        return randomGenerator.nextInt(maxRange);
    }


    /**
     * Retrieves an agent from the Population.
     *
     * @param index the position of the agent in the population list.
     * @return the agent instance.
     */
    protected final AbstractAgent<State> getAgent(final int index) {
        return population.getAgent(index);
    }

    /**
     * Returns a Set that of protocol states and the count of agents under this state.
     *
     * @return a Set with (State,Long) entries.
     */
    protected final Set<Map.Entry<State, Long>> getStateCount() {
        return population.getStateCount();
    }

    /**
     * Returns a List of agents at the given protocol state.
     *
     * @param state the protocol state.
     * @return a list of agents.
     */
    public final List<AbstractAgent<State>> getAgentsAtState(final State state) {
        return population.getAgentsAtState(state);
    }

    /**
     * Invokes the Protocol to perform an interaction between an initiator agent and a responder agent.
     *
     * @param initiator the initiator of the interaction.
     * @param responder the responder of the interaction.
     */
    protected void interact(final AbstractAgent<State> initiator, final AbstractAgent<State> responder) {
        protocol.interact(initiator, responder);
    }

    /**
     * Access the protocol state transision map.
     *
     * @return A Map of StatePair to StatePair objects.
     */
    public final Map<StatePair<State>, StatePair<State>> getTransisionMap() {
        return ((AbstractProtocol<State>) protocol).getTransisionMap();
    }

    public void interact(List<Integer> l1, List<Integer> l2, long round) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
