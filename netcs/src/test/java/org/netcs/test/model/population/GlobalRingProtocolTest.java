//package org.ppsim.test.model.population;
//
//import org.apache.log4j.Logger;
//import org.junit.Test;
//import org.netcs.model.AbstractExperiment;
//import org.netcs.model.AbstractProtocol;
//import org.netcs.model.StateTriple;
//import org.netcs.model.population.PopulationLink;
//import org.netcs.model.population.PopulationNode;
//import org.netcs.scheduler.AbstractScheduler;
//import org.netcs.scheduler.RandomScheduler;
//
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Random;
//
///**
// * Tests operations on Population.
// */
//public class GlobalRingProtocolTest {
//    /**
//     * a log4j logger to print messages.
//     */
//    protected static final Logger LOGGER = Logger.getLogger(GlobalRingProtocolTest.class);
//
//
//    @Test
//    public void textExperiment() {
//        GlobalRingProtocolExperiment experiment = new GlobalRingProtocolExperiment(10, new GlobalRingProtocol(), new RandomScheduler<String>());
//        experiment.initPopulation();
//        experiment.run();
//    }
//
//    class GlobalRingProtocolExperiment extends AbstractExperiment<String, AbstractProtocol<String>> {
//
//        /**
//         * Default constructor.
//         *
//         * @param size      the number of agents.
//         * @param protocol  the population protocol.
//         * @param scheduler the scheduler.
//         */
//        public GlobalRingProtocolExperiment(final int size, final AbstractProtocol<String> protocol, final AbstractScheduler<String> scheduler) {
//            super(size, protocol, scheduler);
//        }
//
//        @Override
//        protected void initPopulation() {
//            Random rand = new Random();
//            String[] states = new String[]{"q0", "q1", "q2", "l", "w", "l'", "l''", "q1'", "q1''"};
//            Iterator<PopulationNode<String>> nodeIterator = getPopulation().getNodes().iterator();
//            while (nodeIterator.hasNext()) {
//                PopulationNode node = nodeIterator.next();
////                node.setState("q0");
//                node.setState(states[rand.nextInt(states.length)]);
//                LOGGER.debug(node);
//            }
//            Iterator<PopulationLink<String>> edgeIterator = getPopulation().getEdges().iterator();
//            while (edgeIterator.hasNext()) {
//                PopulationLink edge = edgeIterator.next();
//                edge.setState("0");
//                LOGGER.debug(edge);
//            }
//
//        }
//
//        @Override
//        protected boolean checkStability() {
//            Boolean result = false;
//            int count = 0;
//            Collection<PopulationNode<String>> nodes = getPopulation().getNodes();
//            for (PopulationNode<String> node : nodes) {
////                result &= node.getState();
////                if (node.getState()) {
////                    count++;
////                }
//            }
//            LOGGER.info(count);
//            return result;
//        }
//
//        @Override
//        protected void completeExperiment() {
//
//        }
//    }
//
//
//    /**
//     * Boolean
//     * Implements the Aproximate Majority Protocol.
//     */
//    public class GlobalRingProtocol extends AbstractProtocol<String> {
//
//
//        /**
//         * Define the entries of the transition map.
//         */
//        protected void setupTransitionsMap() {
//
////(q0, q0, 0) → (q1 , l, 1)
//            addEntry(new StateTriple<>("q0", "q0", "0"), new StateTriple<>("q1", "l", "1"));
////(l, q0, 0) → (q2 , l, 1)
//            addEntry(new StateTriple<>("l", "q0", "0"), new StateTriple<>("q2", "l", "1"));
////(l, l, 0) → (q2 , w, 1)
//            addEntry(new StateTriple<>("l", "l", "0"), new StateTriple<>("q2", "w", "1"));
////(w, q2, 1) → (q2 , w, 1)
//            addEntry(new StateTriple<>("w", "q2", "1"), new StateTriple<>("q2", "w", "1"));
////(w, q1, 1) → (q2 , l, 1)
//            addEntry(new StateTriple<>("w", "q1", "1"), new StateTriple<>("q2", "l", "1"));
////(l, q1, 0) → (l , q1 , 0)
//            addEntry(new StateTriple<>("l", "q1", "0"), new StateTriple<>("l'", "q1'", "0"));
//
//            //(x' , y, 0) → (x'' , y, 0), for x ∈ {l, q1} and y ∈ {l, w, q1 , q0 }
//            addEntry(new StateTriple<>("l'", "l", "0"), new StateTriple<>("l''", "l", "0"));
//            addEntry(new StateTriple<>("l'", "w", "0"), new StateTriple<>("l''", "w", "0"));
//            addEntry(new StateTriple<>("l'", "q1", "0"), new StateTriple<>("l''", "q1", "0"));
//            addEntry(new StateTriple<>("l'", "q0", "0"), new StateTriple<>("l''", "q0", "0"));
//
//            addEntry(new StateTriple<>("q1'", "l", "0"), new StateTriple<>("q1''", "l", "0"));
//            addEntry(new StateTriple<>("q1'", "w", "0"), new StateTriple<>("q1''", "w", "0"));
//            addEntry(new StateTriple<>("q1'", "q1", "0"), new StateTriple<>("q1''", "q1", "0"));
//            addEntry(new StateTriple<>("q1'", "q0", "0"), new StateTriple<>("q1''", "q0", "0"));
//
//            //(x' , y' , 0) → (x'' , y'' , 0), for x ∈ {l, q1 } and y ∈ {l, q1}
//            addEntry(new StateTriple<>("l'", "l'", "0"), new StateTriple<>("l''", "l''", "0"));
//            addEntry(new StateTriple<>("l'", "q1'", "0"), new StateTriple<>("l''", "q1''", "0"));
//
//            addEntry(new StateTriple<>("q1'", "l'", "0"), new StateTriple<>("q1''", "l''", "0"));
//            addEntry(new StateTriple<>("q1'", "q1'", "0"), new StateTriple<>("q1''", "q1''", "0"));
//
////(l , q1, 1) → (l, q1 , 0)
//            addEntry(new StateTriple<>("l''", "q1'", "1"), new StateTriple<>("l'", "q1'", "0"));
////(l , q1 , 1) → (l, q1 , 0)
//            addEntry(new StateTriple<>("l'", "q1'", "1"), new StateTriple<>("l'", "q1'", "0"));
////(l , q1 , 1) → (l, q1 , 0)
//            addEntry(new StateTriple<>("l''", "q1''", "1"), new StateTriple<>("l'", "q1'", "0"));
//        }
//    }
//
//}
