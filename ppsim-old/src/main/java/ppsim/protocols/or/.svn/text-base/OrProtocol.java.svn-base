package ppsim.protocols.or;

import ppsim.model.AbstractProtocol;
import ppsim.model.StatePair;

/**
 * Implements the One-way Or Protocol.
 */
public class OrProtocol extends AbstractProtocol<Boolean> {

    /**
     * Define the entries of the transision map.
     */
    protected void setupTransisionsMap() {
        // (1,0)->(1,1)
        addEntry(new StatePair<Boolean>(true, false),
                new StatePair<Boolean>(true, true));

        // (0,1)->(1,1)
        addEntry(new StatePair<Boolean>(false, true),
                new StatePair<Boolean>(true, true));

    }

}
