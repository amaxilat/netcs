package org.ppsim.test.model.population;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.ppsim.model.AbstractExperiment;
import org.ppsim.model.AbstractProtocol;
import org.ppsim.model.Scheduler;
import org.ppsim.model.StateTriple;
import org.ppsim.model.population.PopulationLink;
import org.ppsim.model.population.PopulationNode;
import org.ppsim.scheduler.RandomScheduler;

import java.util.Iterator;
import java.util.Random;

/**
 * Tests operations on Population.
 */
public class TestProtoocol {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(TestProtoocol.class);


    @Test
    public void testPopulation() {
        TestExperiment experiment = new TestExperiment(10, new TestProtocol(), new RandomScheduler<Boolean>());

    }

    class TestExperiment extends AbstractExperiment<Boolean, AbstractProtocol<Boolean>> {

        /**
         * Default constructor.
         *
         * @param size      the number of agents.
         * @param protocol  the population protocol.
         * @param scheduler the scheduler.
         */
        public TestExperiment(final int size, final AbstractProtocol<Boolean> protocol, final Scheduler<Boolean> scheduler) {
            super(size, protocol, scheduler);
        }

        @Override
        protected void initPopulation() {
            Random rand = new Random();
            Iterator<PopulationNode<Boolean>> nodeIterator = getPopulation().getNodes().iterator();
            while (nodeIterator.hasNext()) {
                PopulationNode node = nodeIterator.next();
                node.setState(rand.nextBoolean());
                LOGGER.debug(node);
            }
            Iterator<PopulationLink<Boolean>> edgeIterator = getPopulation().getEdges().iterator();
            while (edgeIterator.hasNext()) {
                PopulationLink edge = edgeIterator.next();
                edge.setState(false);
                LOGGER.debug(edge);
            }

        }

        @Override
        protected boolean checkStability() {
            return false;
        }

        @Override
        protected void completeExperiment() {

        }
    }


    /**
     * Boolean
     * Implements the Aproximate Majority Protocol.
     */
    public class TestProtocol extends AbstractProtocol<Boolean> {

        /**
         * Define the entries of the transision map.
         */
        protected void setupTransisionsMap() {

            addEntry(new StateTriple<>(true, true, false),
                    new StateTriple<>(true, true, true));
            addEntry(new StateTriple<>(true, false, false),
                    new StateTriple<>(true, true, true));
            addEntry(new StateTriple<>(false, true, false),
                    new StateTriple<>(true, true, true));

        }

        @Override
        public void interact(PopulationNode initiator, PopulationNode responder, PopulationLink link) {

        }
    }

}
