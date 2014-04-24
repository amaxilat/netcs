//package org.ppsim.test.model.population;
//
//import junit.framework.Assert;
//import org.junit.Test;
//import org.netcs.model.AbstractProtocol;
//import org.netcs.model.StateTriple;
//
///**
// * Created by amaxilatis on 3/1/14.
// */
//public class TransitionMapTest {
//
//    @Test
//    public void testTransitionMapChangedState() {
//        TestProtocol protocol = new TestProtocol();
//        StateTriple<String> testStateTriple = new StateTriple<>("q0", "q0", "0");
//        StateTriple<String> resultTestStateTriple = protocol.getTransitionMap().get(testStateTriple);
//        Assert.assertEquals(resultTestStateTriple, new StateTriple<>("q1", "l", "1"));
//    }
//
//    @Test
//    public void testTransitionMapSameState() {
//        TestProtocol protocol = new TestProtocol();
//        StateTriple<String> testStateTriple = new StateTriple<>("q0", "q1", "0");
//        StateTriple<String> resultTestStateTriple = protocol.getTransitionMap().get(testStateTriple);
//        Assert.assertEquals(resultTestStateTriple, null);
//    }
//
//    /**
//     * Boolean
//     * Implements the Aproximate Majority Protocol.
//     */
//    public class TestProtocol extends AbstractProtocol<String> {
//
//        /**
//         * Define the entries of the transition map.
//         */
//        protected void setupTransitionsMap() {
//            addEntry(new StateTriple<>("q0", "q0", "0"),
//                    new StateTriple<>("q1", "l", "1"));
//        }
//    }
//}
