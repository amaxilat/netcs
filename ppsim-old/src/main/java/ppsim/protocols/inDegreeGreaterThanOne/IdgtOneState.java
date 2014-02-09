/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  19 Αυγ 2011 8:45:23 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.protocols.inDegreeGreaterThanOne;

/**
 * Captures the state of the agents when running the In-Degree-Greater-Than-1 protocol.
 */
public class IdgtOneState {

    /**
     * Initial State 
     */

    public static final IdgtOneState STATE_INIT = new IdgtOneState(state.INIT);

    /**
     * State Initiator
     */

    public static final IdgtOneState STATE_I = new IdgtOneState(state.I);

    /**
     * State Responder
     */

    public static final IdgtOneState STATE_R = new IdgtOneState(state.R);

    /**
     * State Contagious
     */

    public static final IdgtOneState STATE_Y = new IdgtOneState(state.Y);

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
    public IdgtOneState(final state mystate) {
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

        final IdgtOneState idgtOneState = (IdgtOneState) obj;

        return getValue() == idgtOneState.getValue();
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
