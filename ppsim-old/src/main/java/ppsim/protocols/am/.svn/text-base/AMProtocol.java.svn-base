package ppsim.protocols.am;

import ppsim.model.AbstractProtocol;
import ppsim.model.StatePair;

/**
 * Implements the Aproximate Majority Protocol.
 */
public class AMProtocol extends AbstractProtocol<AMState> {

    /**
     * Define the entries of the transision map.
     */
    protected void setupTransisionsMap() {
        //(X,B)->(X,X)
        addEntry(new StatePair<AMState>(AMState.STATE_X, AMState.STATE_B),
                new StatePair<AMState>(AMState.STATE_X, AMState.STATE_X));

        //(B,X)->(X,X)
        addEntry(new StatePair<AMState>(AMState.STATE_B, AMState.STATE_X),
                new StatePair<AMState>(AMState.STATE_X, AMState.STATE_X));

        //(Y,B)->(Y,Y)
        addEntry(new StatePair<AMState>(AMState.STATE_Y, AMState.STATE_B),
                new StatePair<AMState>(AMState.STATE_Y, AMState.STATE_Y));

        //(B,Y)->(B,Y)
        addEntry(new StatePair<AMState>(AMState.STATE_B, AMState.STATE_Y),
                new StatePair<AMState>(AMState.STATE_Y, AMState.STATE_Y));

        //(X,Y)->(X,B)
        addEntry(new StatePair<AMState>(AMState.STATE_X, AMState.STATE_Y),
                new StatePair<AMState>(AMState.STATE_X, AMState.STATE_B));

        //(Y,X)->(Y,B)
        addEntry(new StatePair<AMState>(AMState.STATE_Y, AMState.STATE_X),
                new StatePair<AMState>(AMState.STATE_Y, AMState.STATE_B));

    }
}
