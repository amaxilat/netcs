package ppsim.protocols.le;

import ppsim.model.AbstractProtocol;
import ppsim.model.StatePair;

/**
 * Implements the Leader Election Protocol.
 */
public class LEProtocol extends AbstractProtocol<Boolean> {

    /**
     * Define the entries of the transision map.
     */
    protected void setupTransisionsMap() {
        // (1,1) -> (0,1)
        addEntry(new StatePair<Boolean>(true, true),
                new StatePair<Boolean>(false, true));

        // (0,1) -> (1,0)
        addEntry(new StatePair<Boolean>(false, true),
                new StatePair<Boolean>(true, false));

        // (1,0) -> (0,1)
        addEntry(new StatePair<Boolean>(true, false),
                new StatePair<Boolean>(false, true));
    }

}
