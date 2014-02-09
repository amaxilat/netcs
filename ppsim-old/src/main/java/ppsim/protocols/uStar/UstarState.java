/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  20 Αυγ 2011 12:09:38 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.protocols.uStar;

/**
 * Captures the state of the agents when running the Undirected Star protocol.
 */
public class UstarState {


    /**
     * STATES
     */
    public static final UstarState STATE_Q000 = new UstarState(state.Q000);
    public static final UstarState STATE_Q001 = new UstarState(state.Q001);
    public static final UstarState STATE_Q002 = new UstarState(state.Q002);

    public static final UstarState STATE_Q010 = new UstarState(state.Q010);
    public static final UstarState STATE_Q011 = new UstarState(state.Q011);
    public static final UstarState STATE_Q012 = new UstarState(state.Q012);

    public static final UstarState STATE_Q020 = new UstarState(state.Q020);
    public static final UstarState STATE_Q021 = new UstarState(state.Q021);
    public static final UstarState STATE_Q022 = new UstarState(state.Q022);

    public static final UstarState STATE_Q100 = new UstarState(state.Q100);
    public static final UstarState STATE_Q101 = new UstarState(state.Q101);
    public static final UstarState STATE_Q102 = new UstarState(state.Q102);

    public static final UstarState STATE_Q110 = new UstarState(state.Q110);
    public static final UstarState STATE_Q111 = new UstarState(state.Q111);
    public static final UstarState STATE_Q112 = new UstarState(state.Q112);


    public static final UstarState STATE_Q120 = new UstarState(state.Q120);
    public static final UstarState STATE_Q121 = new UstarState(state.Q121);
    public static final UstarState STATE_Q122 = new UstarState(state.Q122);

    public static final UstarState STATE_Q200 = new UstarState(state.Q200);
    public static final UstarState STATE_Q201 = new UstarState(state.Q201);
    public static final UstarState STATE_Q202 = new UstarState(state.Q202);

    public static final UstarState STATE_Q210 = new UstarState(state.Q210);
    public static final UstarState STATE_Q211 = new UstarState(state.Q211);
    public static final UstarState STATE_Q212 = new UstarState(state.Q212);

    public static final UstarState STATE_Q220 = new UstarState(state.Q220);
    public static final UstarState STATE_Q221 = new UstarState(state.Q221);
    public static final UstarState STATE_Q222 = new UstarState(state.Q222);

    public static final UstarState STATE_Z = new UstarState(state.Z);

    enum state{
        /**
         * protocol states: e.g. Q102->(1,(0,2))
         */
        Q000,
        Q001,
        Q002,

        Q010,
        Q011,
        Q012,

        Q020,
        Q021,
        Q022,

        Q100,
        Q101,
        Q102,

        Q110,
        Q111,
        Q112,

        Q120,
        Q121,
        Q122,

        Q200,
        Q201,
        Q202,

        Q210,
        Q211,
        Q212,

        Q220,
        Q221,
        Q222,

        Z
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
    public UstarState(final state mystate) {
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

        final UstarState ustarState = (UstarState) obj;

        return getValue() == ustarState.getValue();
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
