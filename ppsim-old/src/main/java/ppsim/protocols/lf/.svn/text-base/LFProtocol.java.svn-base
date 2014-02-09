package ppsim.protocols.lf;

import ppsim.model.AbstractProtocol;
import ppsim.model.StatePair;

/**
 * Implements the Leader-Follower Protocol.
 */
public class LFProtocol extends AbstractProtocol<LFState> {

    /**
     * Define the entries of the transision map.
     */
    protected void setupTransisionsMap() {
        // (L,F)->(0,0)
        addEntry(new StatePair<LFState>(LFState.STATE_L, LFState.STATE_F),
                new StatePair<LFState>(LFState.STATE_ZERO, LFState.STATE_ZERO));

        // (F,L)->(0,0)
        addEntry(new StatePair<LFState>(LFState.STATE_F, LFState.STATE_L),
                new StatePair<LFState>(LFState.STATE_ZERO, LFState.STATE_ZERO));

        // (L,0)->(L,1)
        addEntry(new StatePair<LFState>(LFState.STATE_L, LFState.STATE_ZERO),
                new StatePair<LFState>(LFState.STATE_L, LFState.STATE_ONE));

        // (0,L)->(1,L)
        addEntry(new StatePair<LFState>(LFState.STATE_ZERO, LFState.STATE_L),
                new StatePair<LFState>(LFState.STATE_ONE, LFState.STATE_L));

        // (L,1)->(1,L)
        addEntry(new StatePair<LFState>(LFState.STATE_L, LFState.STATE_ONE),
                new StatePair<LFState>(LFState.STATE_ONE, LFState.STATE_L));

        // (1,L)->(L,1)
        addEntry(new StatePair<LFState>(LFState.STATE_ONE, LFState.STATE_L),
                new StatePair<LFState>(LFState.STATE_L, LFState.STATE_ONE));

        // (F,1)->(F,0)
        addEntry(new StatePair<LFState>(LFState.STATE_F, LFState.STATE_ONE),
                new StatePair<LFState>(LFState.STATE_F, LFState.STATE_ZERO));

        // (1,F)->(0,F)
        addEntry(new StatePair<LFState>(LFState.STATE_ONE, LFState.STATE_F),
                new StatePair<LFState>(LFState.STATE_ZERO, LFState.STATE_F));

        // (F,0)->(0,F)
        addEntry(new StatePair<LFState>(LFState.STATE_F, LFState.STATE_ZERO),
                new StatePair<LFState>(LFState.STATE_ZERO, LFState.STATE_F));

        // (0,F)->(F,0)
        addEntry(new StatePair<LFState>(LFState.STATE_ZERO, LFState.STATE_F),
                new StatePair<LFState>(LFState.STATE_F, LFState.STATE_ZERO));

        // (0,1)->(0,0)
        addEntry(new StatePair<LFState>(LFState.STATE_ZERO, LFState.STATE_ONE),
                new StatePair<LFState>(LFState.STATE_ZERO, LFState.STATE_ZERO));

        // (1,0)->(0,0)
        addEntry(new StatePair<LFState>(LFState.STATE_ONE, LFState.STATE_ZERO),
                new StatePair<LFState>(LFState.STATE_ZERO, LFState.STATE_ZERO));
    }

}
