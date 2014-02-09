/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  28 Αυγ 2011 3:27:11 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.protocols.twoCycle;

/**
 * Captures the state of the agents when running the TwoCycle protocol.
 */
public class TwoCycleState {

    /**
     * STATES
     */
    public static final TwoCycleState STATE_LS = new TwoCycleState(state.LS);
    public static final TwoCycleState STATE_LC = new TwoCycleState(state.LC);
    public static final TwoCycleState STATE_LD = new TwoCycleState(state.LD);
    public static final TwoCycleState STATE_FW = new TwoCycleState(state.FW);
    public static final TwoCycleState STATE_FP = new TwoCycleState(state.FP);
    public static final TwoCycleState STATE_FD = new TwoCycleState(state.FD);

        enum state{
        /**
         * protocol states: e.g. Q102->(1,(0,2))
         */
            LS,
            LC,
            LD,
            FW,
            FP,
            FD
        }

    /**
     * Keeps track of the actual state.
     */
    private state myvalue;

    /**
     * Default Constructor.
     *
     * @param mystate the initial state.
     */
    public TwoCycleState(final state mystate) {
        this.myvalue = mystate;
    }

    /**
     * Get the value of the state.
     *
     * @return the value of the state.
     */
    public state getValue() {
        return myvalue;
    }

    /**
     * Set the value of the state.
     *
     * @param mystate the new value of the state.
     */
    public void setValue(final state mystate) {
        this.myvalue = mystate;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final TwoCycleState twoCycleState = (TwoCycleState) obj;

        return getValue() == twoCycleState.getValue();
    }

    @Override
    public int hashCode() {
        if (getValue() == null) {
            return 0;
        } else {
            return getValue().hashCode();
        }
    }
}
