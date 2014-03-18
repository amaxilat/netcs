package org.ppsim.test.model.population;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.ppsim.model.AbstractExperiment;
import org.ppsim.model.AbstractProtocol;
import org.ppsim.model.StateTriple;
import org.ppsim.model.population.PopulationLink;
import org.ppsim.model.population.PopulationNode;
import org.ppsim.scheduler.AbstractScheduler;
import org.ppsim.scheduler.RandomScheduler;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * Tests operations on Population.
 */
public class LineProtoocol {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(LineProtoocol.class);


    @Test
    public void testPopulation() {
        TestExperiment experiment = new TestExperiment(10, new TestProtocol(), new RandomScheduler<String>());
        experiment.initPopulation();
        experiment.run();
    }

    class TestExperiment extends AbstractExperiment<String, AbstractProtocol<String>> {

        /**
         * Default constructor.
         *
         * @param size      the number of agents.
         * @param protocol  the population protocol.
         * @param scheduler the scheduler.
         */
        public TestExperiment(final int size, final AbstractProtocol<String> protocol, final AbstractScheduler<String> scheduler) {
            super(size, protocol, scheduler);
        }

        @Override
        protected void initPopulation() {
            Random rand = new Random();
            String[] states = new String[]{"q0", "q1", "q2", "l", "w"};
            Iterator<PopulationNode<String>> nodeIterator = getPopulation().getNodes().iterator();
            while (nodeIterator.hasNext()) {
                PopulationNode node = nodeIterator.next();
//                node.setState("q0");
                node.setState(states[rand.nextInt(states.length)]);
                LOGGER.debug(node);
            }
            Iterator<PopulationLink<String>> edgeIterator = getPopulation().getEdges().iterator();
            while (edgeIterator.hasNext()) {
                PopulationLink edge = edgeIterator.next();
                edge.setState("0");
                LOGGER.debug(edge);
            }

        }

        @Override
        protected boolean checkStability() {
            Boolean result = false;
            int count = 0;
            Collection<PopulationNode<String>> nodes = getPopulation().getNodes();
            for (PopulationNode<String> node : nodes) {
//                result &= node.getState();
//                if (node.getState()) {
//                    count++;
//                }
            }
            LOGGER.info(count);
            return result;
        }

        @Override
        protected void completeExperiment() {

        }
    }


    /**
     * Boolean
     * Implements the Aproximate Majority Protocol.
     */
    public class TestProtocol extends AbstractProtocol<String> {

        /**
         * Define the entries of the transition map.
         */
        protected void setupTransitionsMap() {
            addEntry(new StateTriple<>("q0", "q0", "0"),
                    new StateTriple<>("q1", "l", "1"));
            addEntry(new StateTriple<>("l", "q0", "0"),
                    new StateTriple<>("q2", "l", "1"));
            addEntry(new StateTriple<>("l", "l", "0"),
                    new StateTriple<>("q2", "w", "1"));
            addEntry(new StateTriple<>("w", "q2", "1"),
                    new StateTriple<>("q2", "w", "1"));
            addEntry(new StateTriple<>("w", "q1", "1"),
                    new StateTriple<>("q2", "l", "1"));
        }
    }

}
