package org.netcs.model;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.netcs.config.ConfigFile;
import org.netcs.config.Transition;
import org.netcs.model.population.MemoryPopulation;
import org.netcs.model.population.Population;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;
import org.netcs.scheduler.AbstractScheduler;
import org.netcs.service.LookupService;
import org.springframework.messaging.core.MessageSendingOperations;

import java.util.*;


/**
 * Executes a simple experiment.
 *
 * @param the        variable type for the state of the agent.
 * @param <Protocol> the protocol executed in the experiment.
 */
public abstract class AbstractExperiment<State, Protocol extends AbstractProtocol> extends Thread {

    /**
     * Apache Log4J logger.
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractExperiment.class);
    private final long index;

    private static final long B_ADVANTAGE = 1;

    /**
     * Current interactions of experiment.
     */
    private long interactions = 0;
    private long effectiveInteractions = 0;

    /**
     * Population holder.
     */
    private final Population population;

    /**
     * Actual protocol.
     */
    private final AbstractProtocol protocol;

    /**
     * Scheduler.
     */
    private final Scheduler scheduler;
    private final ConfigFile configFile;
    protected String resultString;

    protected boolean lookingForStar;
    protected boolean lookingForCircle;
    protected boolean lookingForCycleCover;
    protected boolean lookingForLine;
    protected boolean lookingForSize;
    protected boolean finished;
    private boolean success;
    private String terminationMessage;
    private Map<String, String> terminationStats;
    private PopulationNode leaderNode;
    private MessageSendingOperations<String> messagingTemplate;
    private long cardinalityStart;
    private boolean hadCardinality;
    final SummaryStatistics cardinalityFactorStatistics;
    private boolean lookingForLeader;

    /**
     * Default constructor.
     *
     * @param protocol  the population protocol.
     * @param scheduler the scheduler.
     */
    public AbstractExperiment(final ConfigFile configFile, final AbstractProtocol protocol, final AbstractScheduler scheduler, long index, final LookupService lookupService) {
        // Construct population
        this.protocol = protocol;
        this.population = new MemoryPopulation(configFile.getPopulationSize());
        this.configFile = configFile;
        this.index = index;
        this.success = false;
        // Initialize Population
        initPopulation();

        scheduler.connect(population, protocol);
        // Connect scheduler
        this.scheduler = scheduler;
        //this.scheduler.connect(this.population, this.protocol);

        this.lookingForStar = false;
        this.lookingForCircle = false;
        this.lookingForCycleCover = false;
        this.lookingForLeader = false;
        this.lookingForLine = false;
        this.finished = false;
        this.terminationStats = new HashMap<>();
        this.hadCardinality = false;
        cardinalityFactorStatistics = new SummaryStatistics();
        //reportStatus("initialized");
    }

    /**
     * Initialize the population.
     */
    public void initPopulation() {
        final long start = System.currentTimeMillis();

        if (configFile.getInitialNodeState().equals("?")) {
            final Set<String> states = new HashSet<>();
            for (final Transition transition : configFile.getTransitions()) {
                states.add(transition.getNode1());
                states.add(transition.getNode1New());
                states.add(transition.getNode2());
                states.add(transition.getNode2New());
            }
            final String[] statesArray = states.toArray(new String[states.size()]);
            final Random rand = new Random();
            int count = 0;
            final Iterator<PopulationNode> nodeIterator = getPopulation().getNodes().iterator();
            while (nodeIterator.hasNext()) {
                if (count < states.size()) {
                    final PopulationNode node = nodeIterator.next();
                    node.setState(statesArray[count]);
                    LOGGER.debug(node);
                } else {
                    final PopulationNode node = nodeIterator.next();
                    node.setState(statesArray[rand.nextInt(states.size())]);
                    LOGGER.debug(node);
                }
                count++;
            }
        } else {
            final Iterator<PopulationNode> nodeIterator = getPopulation().getNodes().iterator();
            while (nodeIterator.hasNext()) {
                final PopulationNode node = nodeIterator.next();
                node.setState(configFile.getInitialNodeState());
                LOGGER.debug(node);
            }
        }


        final Iterator<PopulationLink> edgeIterator = getPopulation().getEdges().iterator();
        while (edgeIterator.hasNext()) {
            final PopulationLink edge = edgeIterator.next();
            edge.setState(configFile.getInitialLinkState());
            LOGGER.debug(edge);
        }

        population.initCache(index);
        LOGGER.info("initPopulation:" + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * Start the execution of the experiment.
     */
    public void run() {
        //reportStatus("started");
        final long start = System.currentTimeMillis();
        SummaryStatistics interactionTimes = new SummaryStatistics();
        SummaryStatistics stabilityCheckTimes = new SummaryStatistics();
        while (true) {
            try {
                final long startInteraction = System.currentTimeMillis();
                // Invoke scheduler to conduct next interaction
                boolean interactionStatus = scheduler.interact(index);

//                checkCardinalities();

                if (interactionStatus) {

                    //printExperimentStatus();
                    effectiveInteractions++;
                    // produce debug information
                    if (LOGGER.getLevel() == Level.DEBUG) {
                        debugRound();
                    }
                    final long startCheckStability = System.currentTimeMillis();
                    LOGGER.debug(String.format("[%d] checking stability...", index));
                    final boolean stability = checkStability();
                    LOGGER.debug(String.format("[%d] %s checked stability for %d", index, stability, (System.currentTimeMillis() - startCheckStability)));
                    //reportStatus(String.format("[%d] %d:%s:%s", index, interactions, interactionStatus, stability));
                    // Check if we have reached a stable state
                    if (stability) {
                        break;
                    }
                    long stabilityCheckTime = System.currentTimeMillis() - startCheckStability;
                    stabilityCheckTimes.addValue(stabilityCheckTime);
                    reportStatus(String.format("[%d] checkStability %dms", index, stabilityCheckTime));
                }

                // increase interactions counter
                interactions++;

                //exit after too many interactions
                if (interactions > 2000000) {
                    finished = true;
                    success = false;
                    break;
                }
//                //exit after a lot of interactions and cardinality
//                if (interactions > getPopulationSize() * 10000 && terminationStats.containsKey("cardinalityExistsFactor")) {
//                    finished = true;
//                    break;
//                }
                long time = (System.currentTimeMillis() - startInteraction);
                interactionTimes.addValue(time);
                if (effectiveInteractions % 10000 == 0) {
                    LOGGER.debug(String.format("[%d] stats: %.2f/%.2f/%.2f %.2f/%.2f/%.2f", index,
                            interactionTimes.getMean(), interactionTimes.getMax(), interactionTimes.getMin(),
                            stabilityCheckTimes.getMean(), stabilityCheckTimes.getMax(), stabilityCheckTimes.getMin()
                    ));
                }
            } catch (Exception ex) {
                LOGGER.error("Exception occurred", ex);
            }
        }
        final long totalTime = (System.currentTimeMillis() - start);
        terminationStats.put("time", Long.toString(totalTime));

        LOGGER.info(String.format("[%d] finalStats: %.0f|%d %.2f/%.2f/%.2f %.2f/%.2f/%.2f", index,
                interactionTimes.getMean() * interactions, totalTime,
                interactionTimes.getMean(), interactionTimes.getMax(), interactionTimes.getMin(),
                stabilityCheckTimes.getMean(), stabilityCheckTimes.getMax(), stabilityCheckTimes.getMin()
        ));


        // Finalize experiment
        completeExperiment();

        reportStatus(String.format("[%d] completed-interactions %d", index, interactions));
        reportStatus(String.format("[%d] completed-time%dms", index, System.currentTimeMillis() - start));
    }

    protected void printExperimentStatus() {
        for (PopulationLink statePopulationLink : getPopulation().getEdges()) {
            LOGGER.info(statePopulationLink);
        }
    }

    /**
     * Evaluates if the population protocol has reached a stable state.
     *
     * @return true if the protocol has reached a stable state.
     */
    protected boolean checkStability() {
        LOGGER.debug("lookingForSize:" + lookingForSize + " " + lookingForStar);
//
//        if (checkSizes(B_ADVANTAGE)) {
//            finished = true;
//            return true;
//        }

        if (lookingForStar && checkStar()) {
            LOGGER.info("<================ FOUND STAR");
            finished = true;
            success = true;
            return true;
        }
        if (lookingForCircle && checkCircle()) {
            LOGGER.info("<================ FOUND CIRCLE");
            finished = true;
            success = true;
            return true;
        }
        if (lookingForCycleCover && checkCycleCover()) {
            LOGGER.info("<================ FOUND CCOVER");
            finished = true;
            success = true;
            return true;
        }
        if (lookingForLeader && checkLeader()) {
            LOGGER.info("<================ FOUND LEADER");
            finished = true;
            success = true;
            return true;
        }
        if (lookingForLine && checkLine()) {
            LOGGER.info("<================ FOUND LINE");
            finished = true;
            success = true;
            return true;
        }
        finished = false;
        return false;
    }


    /**
     * Finalizes the execution of the experiment.
     */
    protected abstract void completeExperiment();

    /**
     * Prepares a debug output with the total number of agents per protocol state.
     */
    void debugRound() {
//        final StringBuffer strbuf = new StringBuffer();
//        strbuf.append(interactions);
//        final Set<Map.Entry<State, Long>> states = population.getStateCount();
//        for (Map.Entry<State, Long> state : states) {
//            strbuf.append(" [");
//            strbuf.append(state.getKey());
//            strbuf.append(" , ");
//            strbuf.append(state.getValue());
//            strbuf.append(']');
//        }
//        LOGGER.debug(strbuf.toString());
    }

    /**
     * The size of the population.
     *
     * @return the number of agents.
     */
    public int getPopulationSize() {
        return population.size();
    }

    /**
     * The interactions of execution.
     *
     * @return the number of rounds executed.
     */
    public long getInteractions() {
        return interactions;
    }

    /**
     * The population protocol holder.
     *
     * @return the population holder.
     */
    public Population getPopulation() {
        return population;
    }

    /**
     * Access the name of the scheduler.
     *
     * @return the class name of the scheduler.
     */
    protected final String getSchedulerName() {
        return scheduler.getClass().getSimpleName();
    }

    private void reportStatus(final String status) {
        LOGGER.debug(status);
    }

    public String getResultString() {
        return resultString;
    }

    public long getEffectiveInteractions() {
        return effectiveInteractions;
    }


    protected boolean checkCycleCover() {
        reportStatus(String.format("[%d] check-cycleCover", index));

        Boolean result = false;
        long edgeCount;
        long degreeZeroNodes = 0;
        long degreeTwoNodes = 0;
        long totalDegree = 0;

        for (int i = 0; i < getPopulationSize(); i++) {
            final long nodeDegree = getPopulation().getDegree(getPopulation().getAgent(i));
            if (nodeDegree == 0) {
                degreeZeroNodes++;
            } else if (nodeDegree == 2) {
                degreeTwoNodes++;
            } else {
                return false;
            }
            totalDegree += nodeDegree;
        }
        edgeCount = totalDegree / 2;
        reportStatus(String.format("[%d] network-stats {degree2:%d, degreeTotal:%d, edges:%d}", index, degreeTwoNodes, totalDegree, edgeCount));

        //TODO: check for more terminating conditions
        if (degreeTwoNodes + degreeZeroNodes == getPopulationSize()) {
            reportStatus(String.format("[%d] terminating-condition cycleCover", index));
            return true;
        }

//        final StringBuilder nodesStringBuilder = new StringBuilder("Nodes: ");
//        for (final PopulationNode node : getPopulation().getNodes()) {
//            nodesStringBuilder.append(node).append(",");
//        }
//        nodesStringBuilder.append("]");
//        LOGGER.debug(nodesStringBuilder.toString());
//
//        final StringBuilder edgesStringBuilder = new StringBuilder("Edges: ");
//        for (final PopulationLink edge : getPopulation().getEdges()) {
//            if (edge.getState().equals("1")) {
//                edgesStringBuilder.append(edge.getDefaultEdge().toString()).append(",");
//            }
//        }
//        LOGGER.debug(edgesStringBuilder.toString());

        return result;
    }

    protected boolean checkLeader() {
        reportStatus(String.format("[%d] check-leader", index));

        Boolean result = false;
        long leaders = 0;
        for (PopulationNode populationNode : population.getNodes()) {
            if (populationNode.getState().equals("l")) {
                leaders++;
                if (leaders > 1) {
                    return false;
                }
            }
        }
        if (leaders == 1) {
            success = true;
            result = true;
        }
        reportStatus(String.format("[%d] network-stats {leaders:%d}", index, leaders));

        if (result) {
            reportStatus(String.format("[%d] terminating-condition leader", index));
            success = true;
            return true;
        }
        return result;
    }

    protected boolean checkStar() {
        reportStatus(String.format("[%d] check-star", index));

        Boolean result = false;
        long edgeCount;
        long degreeOneNodes = 0;
        long degreeStarCenter = 0;
        long totalDegree = 0;

        final Collection<PopulationNode> nodes = getPopulation().getNodes();
        for (final PopulationNode node : nodes) {
            final long nodeDegree = getPopulation().getDegree(node);
            if (nodeDegree == 1) {
                degreeOneNodes++;
            } else if (nodeDegree == getPopulationSize() - 1) {
                degreeStarCenter++;
            }
            totalDegree += nodeDegree;
        }
        edgeCount = totalDegree / 2;
        reportStatus(String.format("[%d] network-status {degree1:%d, degree*:%d, total:%d, edges:%d}", index, degreeOneNodes, degreeStarCenter, totalDegree, edgeCount));

        //TODO: check for more terminating conditions
        if (edgeCount == getPopulationSize() - 1
                && degreeOneNodes == getPopulationSize() - 1
                && degreeStarCenter == 1) {
            reportStatus(String.format("[%d] terminating-condition star", index));
            return true;
        }

//        final StringBuilder nodesStringBuilder = new StringBuilder("Nodes: ");
//        for (final PopulationNode node : getPopulation().getNodes()) {
//            nodesStringBuilder.append(node).append(",");
//        }
//        nodesStringBuilder.append("]");
//        LOGGER.debug(nodesStringBuilder.toString());
//
//        final StringBuilder edgesStringBuilder = new StringBuilder("Edges: ");
//        for (final PopulationLink edge : getPopulation().getEdges()) {
//            if (edge.getState().equals("1")) {
//                edgesStringBuilder.append(edge.getDefaultEdge().toString()).append(",");
//            }
//        }
//        LOGGER.debug(edgesStringBuilder.toString());

        return result;
    }

    protected boolean checkCircle() {
        reportStatus(String.format("[%d] check-circle", index));

        Boolean result = false;

        long degreeTwoNodes = 0;

        final Collection<PopulationNode> nodes = getPopulation().getNodes();
        for (final PopulationNode node : nodes) {
            if (getPopulation().getDegree(node) == 2) {
                degreeTwoNodes++;
            } else {
                return false;
            }
        }

        final PopulationNode startingNode = getPopulation().getNodes().iterator().next();
        final Set<String> allNodes = new HashSet<>();
        final Set<PopulationNode> pendingNodes = new HashSet<>();
        allNodes.add(startingNode.getNodeName());
        pendingNodes.add(startingNode);
        PopulationNode currentNode;
        while (!pendingNodes.isEmpty()) {
            currentNode = pendingNodes.iterator().next();
            for (PopulationNode otherNode : getPopulation().getNodes()) {
                final PopulationLink edge = getPopulation().getEdge(currentNode, otherNode);
                if (edge != null && edge.getState().equals("1")) {
                    if (!allNodes.contains(otherNode.getNodeName())) {
                        pendingNodes.add(otherNode);
                        allNodes.add(otherNode.getNodeName());
                    }
                }
                final PopulationLink edgeInv = getPopulation().getEdge(otherNode, currentNode);
                if (edgeInv != null && edgeInv.getState().equals("1")) {
                    if (!allNodes.contains(otherNode.getNodeName())) {
                        pendingNodes.add(otherNode);
                        allNodes.add(otherNode.getNodeName());
                    }
                }
            }
            pendingNodes.remove(currentNode);
        }

        reportStatus(String.format("[%d] network-status {degree2:%d, connectedNodes:%d}", index, degreeTwoNodes, allNodes.size()));

        //TODO: check for more terminating conditions
        if (degreeTwoNodes == getPopulationSize() && allNodes.size() == getPopulationSize()) {
            reportStatus(String.format("[%d] terminating-condition circle", index));
            return true;
        }

//        final StringBuilder nodesStringBuilder = new StringBuilder("Nodes: ");
//        for (final PopulationNode node : getPopulation().getNodes()) {
//            nodesStringBuilder.append(node).append(",");
//        }
//        nodesStringBuilder.append("]");
//        LOGGER.debug(nodesStringBuilder.toString());
//
//        final StringBuilder edgesStringBuilder = new StringBuilder("Edges: ");
//        for (final PopulationLink edge : getPopulation().getEdges()) {
//            if (edge.getState().equals("1")) {
//                edgesStringBuilder.append(edge.getDefaultEdge().toString()).append(",");
//            }
//        }
//        LOGGER.debug(edgesStringBuilder.toString());

        return result;
    }

    protected boolean checkSizes(final long b) {
        //reportStatus(String.format("[%d] check-sizes", index));

        final Collection<PopulationNode> nodes = getPopulation().getNodes();
        for (final PopulationNode node : nodes) {
            if (node.getState().equals("l")) {
                //reportStatus(String.format("[%d] %d>%d && %d==%d", index, node.getCount1(), b, node.getCount1(), node.getCount2()));
                if (node.getCount1() > b && node.getCount1() == node.getCount2()) {
                    if (node.getCount1() > getPopulationSize() / 2) {
                        success = true;
                    } else {
                        success = false;
                    }
                    terminationMessage = "b=" + B_ADVANTAGE + ",count1=" + node.getCount1() + ",count2=" + node.getCount2();

                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    protected Boolean checkLine() {
        reportStatus(String.format("[%d] check-line", index));

        Boolean result = false;
        long edgeCount;
        long degreeOneNodes = 0;
        long degreeTwoNodes = 0;
        long totalDegree = 0;

        final Collection<PopulationNode> nodes = getPopulation().getNodes();
        for (final PopulationNode node : nodes) {
            final long nodeDegree = getPopulation().getDegree(node);
            if (nodeDegree == 1) {
                degreeOneNodes++;
            } else if (nodeDegree == 2) {
                degreeTwoNodes++;
            }
            totalDegree += nodeDegree;
        }
        edgeCount = totalDegree / 2;
        reportStatus(String.format("[%d] network-status {degree1:%d, degree2:%d, total:%d, edges:%d}", index, degreeOneNodes, degreeTwoNodes, totalDegree, edgeCount));

        //TODO: check for more terminating conditions
        if (edgeCount == getPopulationSize() - 1
                && degreeOneNodes == 2
                && degreeTwoNodes == getPopulationSize() - 2) {
            reportStatus(String.format("[%d] terminating-condition line", index));
            return true;
        }

//        final StringBuilder nodesStringBuilder = new StringBuilder("Nodes: ");
//        for (final PopulationNode node : getPopulation().getNodes()) {
//            nodesStringBuilder.append(node).append(",");
//        }
//        nodesStringBuilder.append("]");
//        LOGGER.debug(nodesStringBuilder.toString());
//
//        final StringBuilder edgesStringBuilder = new StringBuilder("Edges: ");
//        for (final PopulationLink edge : getPopulation().getEdges()) {
//            if (edge.getState().equals("1")) {
//                edgesStringBuilder.append(edge.getDefaultEdge().toString()).append(",");
//            }
//        }
//        LOGGER.debug(edgesStringBuilder.toString());

        return result;
    }

    protected void checkCardinalities() {

        final Collection<PopulationNode> nodes = getPopulation().getNodes();
        final Map<String, SummaryStatistics> stateCounts = new HashMap<>();
        for (PopulationNode node : nodes) {
            if (!stateCounts.containsKey(node.getState())) {
                stateCounts.put(node.getState(), new SummaryStatistics());
            }
            stateCounts.get(node.getState()).addValue(1);
        }
        final double acceptedLevel = getPopulationSize() / (double) stateCounts.keySet().size() / ((1.5) * getPopulationSize());
        StringBuilder statesBuilder = new StringBuilder("StateCounts(" + acceptedLevel + "):[ ");
        boolean highCardinalities = true;
        for (String state : stateCounts.keySet()) {
            final double cardinality = stateCounts.get(state).getSum() / getPopulationSize();
            statesBuilder.append(state).append("=").append(cardinality).append(" ");
            if (cardinality < acceptedLevel) {
                highCardinalities = false;
            }
        }
        statesBuilder.append("]");
        if (highCardinalities) {

            final long currentInteraction = this.getInteractions();
            if (hadCardinality) {
                final long cardinalityLength = currentInteraction - cardinalityStart;
                if (cardinalityLength > getPopulationSize()) {

                    final double factor = ((double) cardinalityLength / (double) getPopulationSize());
                    final double prevMax = cardinalityFactorStatistics.getMax();
                    cardinalityFactorStatistics.addValue(factor);
                    if (prevMax < cardinalityFactorStatistics.getMax()) {
                        LOGGER.debug(statesBuilder.toString());
                        LOGGER.debug("Interactions: " + interactions + " Factor:" + factor + " max:" + cardinalityFactorStatistics.getMax());
                        terminationStats.put("cardinalityExistsFactor", String.valueOf(cardinalityFactorStatistics.getMax()));
                    }
                }
            } else {
                hadCardinality = true;
                cardinalityStart = currentInteraction;
            }
        } else {
            hadCardinality = false;
//            LOGGER.info(statesBuilder.toString());
        }
    }

    public boolean isLookingForStar() {
        return lookingForStar;
    }

    public void setLookingForStar(boolean lookingForStar) {
        this.lookingForStar = lookingForStar;
    }

    public boolean isLookingForCircle() {
        return lookingForCircle;
    }

    public void setLookingForCircle(boolean lookingForCircle) {
        this.lookingForCircle = lookingForCircle;
    }

    public boolean isLookingForCycleCover() {
        return lookingForCycleCover;
    }

    public boolean isLookingForLeader() {
        return lookingForLeader;
    }

    public void setLookingForCycleCover(boolean lookingForCycleCover) {
        this.lookingForCycleCover = lookingForCycleCover;
    }

    public void setLookingForLeader(boolean lookingForLeader) {
        this.lookingForLeader = lookingForLeader;
    }

    public boolean isLookingForLine() {
        return lookingForLine;
    }

    public void setLookingForLine(boolean lookingForLine) {
        this.lookingForLine = lookingForLine;
    }

    public boolean isLookingForSize() {
        return lookingForSize;
    }

    public void setLookingForSize(boolean lookingForSize) {
        this.lookingForSize = lookingForSize;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getTerminationMessage() {
        return terminationMessage;
    }

    public Map<String, String> getTerminationStats() {
        return terminationStats;
    }

}
