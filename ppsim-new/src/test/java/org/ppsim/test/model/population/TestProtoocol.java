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
public class TestProtoocol {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(TestProtoocol.class);


    @Test
    public void testPopulation() {
        TestExperiment experiment = new TestExperiment(10, new TestProtocol(), new RandomScheduler<Boolean>());
        experiment.initPopulation();
        experiment.run();
    }

    class TestExperiment extends AbstractExperiment<Boolean, AbstractProtocol<Boolean>> {

        /**
         * Default constructor.
         *
         * @param size      the number of agents.
         * @param protocol  the population protocol.
         * @param scheduler the scheduler.
         */
        public TestExperiment(final int size, final AbstractProtocol<Boolean> protocol, final AbstractScheduler<Boolean> scheduler) {
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
            Boolean result = true;
            int count = 0;
            Collection<PopulationNode<Boolean>> nodes = getPopulation().getNodes();
            for (PopulationNode<Boolean> node : nodes) {
                result &= node.getState();
                if (node.getState()) {
                    count++;
                }
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
    public class TestProtocol extends AbstractProtocol<Boolean> {

        /**
         * Define the entries of the transision map.
         */
        protected void setupTransitionsMap() {

            addEntry(new StateTriple<>(true, true, false),
                    new StateTriple<>(true, true, true));
            addEntry(new StateTriple<>(true, false, false),
                    new StateTriple<>(true, true, true));
            addEntry(new StateTriple<>(false, true, false),
                    new StateTriple<>(true, true, true));

        }
    }

}
