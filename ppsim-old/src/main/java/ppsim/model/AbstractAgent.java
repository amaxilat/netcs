package ppsim.model;

/**
 * Represent a population protocol agent.
 *
 * @param <State> the variable type for the state of the agent.
 */
public abstract class AbstractAgent<State> {

    /**
     * Population holder.
     */
    private final Population<State> population;

    /**
     * The internal state of the agent.
     */
    private State state;

    /**
     * Internal identity for record keeping.
     */
    private int agentID;

    /**
     * Empty constructor.
     */
    public AbstractAgent() {
        population = null;
        agentID = -1;
    }

    /**
     * Default constructor.
     *
     * @param pop the Population holder.
     */
    public AbstractAgent(final Population<State> pop) {
        population = pop;
        population.addAgent(this);
        initAgent();
    }

    /**
     * Get the state of the agent.
     *
     * @return the state of the agent.
     */
    public State getState() {
        return state;
    }

    /**
     * Set the state of the agent.
     *
     * @param newState the new state of the agent.
     */
    public void setState(final State newState) {
        population.removeFromState(this);
        state = newState;
        population.addToState(this);
    }

    /**
     * Get internal agent counter.
     *
     * @return agent counter.
     */
    public int getAgentID() {
        return agentID;
    }

    /**
     * Set the agent counter.
     *
     * @param newId the agent counter.
     */
    public void setAgentID(final int newId) {
        this.agentID = newId;
    }

    /**
     * Initializes the state of the agent.
     */
    protected abstract void initAgent();
}
