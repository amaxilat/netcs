/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  28 Αυγ 2011 5:04:48 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.protocols.outDegreeGreaterThanOne;

/**
 * Captures the state of the agents when running the Outn-Degree-Greater-Than-1 protocol.
 */
public class OdgtOneState {

    /**
     * Initial State
     */

    public static final OdgtOneState STATE_INIT = new OdgtOneState(state.INIT);

    /**
     * State Initiator
     */

    public static final OdgtOneState STATE_I = new OdgtOneState(state.I);

    /**
     * State Responder
     */

    public static final OdgtOneState STATE_R = new OdgtOneState(state.R);

    /**
     * State Contagious
     */

    public static final OdgtOneState STATE_Y = new OdgtOneState(state.Y);

    /**
     * The different states.
     */
    enum state {
        /**
         * INIT, I, R, Y protocol states.
         */
        INIT, I, R, Y
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
    public OdgtOneState(final state mystate) {
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

        final OdgtOneState odgtOneState = (OdgtOneState) obj;

        return getValue() == odgtOneState.getValue();
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
