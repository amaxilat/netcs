package org.ppsim.model;

import org.apache.log4j.Logger;

/**
 * Represents a pair of states.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class StateTriple<State> {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(StateTriple.class);

    /**
     * The state of the initiator.
     */
    private State initiator;

    /**
     * The state of the responder.
     */
    private State responder;
    /**
     * The state of the link.
     */
    private State link;

    /**
     * Default constructor.
     *
     * @param init the state of the initiator.
     * @param resp the state of the responder.
     */
    public StateTriple(final State init, final State resp, final State link) {
        this.initiator = init;
        this.responder = resp;
        this.link = link;
    }

    /**
     * Get the state of the initiator.
     *
     * @return the state of the initiator.
     */
    public State getInitiatorState() {
        return initiator;
    }

    /**
     * Set the state of the initiator.
     *
     * @param init the state of the initiator.
     */
    public void setInitiator(final State init) {
        this.initiator = init;
    }

    /**
     * Get the state of the responder.
     *
     * @return the state of the responder.
     */
    public State getResponderState() {
        return responder;
    }

    /**
     * Set the state of the responder.
     *
     * @param resp the state of the responder.
     */
    public void setResponder(final State resp) {
        this.responder = resp;
    }

    public State getLinkState() {
        return link;
    }

    public void setLink(State link) {
        this.link = link;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p/>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     * <code>x</code>, <code>x.equals(x)</code> should return
     * <code>true</code>.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     * <code>x</code> and <code>y</code>, <code>x.equals(y)</code>
     * should return <code>true</code> if and only if
     * <code>y.equals(x)</code> returns <code>true</code>.
     * <li>It is <i>transitive</i>: for any non-null reference values
     * <code>x</code>, <code>y</code>, and <code>z</code>, if
     * <code>x.equals(y)</code> returns <code>true</code> and
     * <code>y.equals(z)</code> returns <code>true</code>, then
     * <code>x.equals(z)</code> should return <code>true</code>.
     * <li>It is <i>consistent</i>: for any non-null reference values
     * <code>x</code> and <code>y</code>, multiple invocations of
     * <tt>x.equals(y)</tt> consistently return <code>true</code>
     * or consistently return <code>false</code>, provided no
     * information used in <code>equals</code> comparisons on the
     * objects is modified.
     * <li>For any non-null reference value <code>x</code>,
     * <code>x.equals(null)</code> should return <code>false</code>.
     * </ul>
     * <p/>
     * The <tt>equals</tt> method for class <code>Object</code> implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values <code>x</code> and
     * <code>y</code>, this method returns <code>true</code> if and only
     * if <code>x</code> and <code>y</code> refer to the same object
     * (<code>x == y</code> has the value <code>true</code>).
     * <p/>
     * Note that it is generally necessary to override the <tt>hashCode</tt>
     * method whenever this method is overridden, so as to maintain the
     * general contract for the <tt>hashCode</tt> method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise.
     * @see #hashCode()
     * @see java.util.Hashtable
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final StateTriple otherPair = (StateTriple) obj;

        return getInitiatorState().equals(otherPair.getInitiatorState())
                && getResponderState().equals(otherPair.getResponderState())
                && getLinkState().equals(otherPair.getLinkState());
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * <code>java.util.Hashtable</code>.
     * <p/>
     * The general contract of <code>hashCode</code> is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     * an execution of a Java application, the <tt>hashCode</tt> method
     * must consistently return the same integer, provided no information
     * used in <tt>equals</tt> comparisons on the object is modified.
     * This integer need not remain consistent from one execution of an
     * application to another execution of the same application.
     * <li>If two objects are equal according to the <tt>equals(Object)</tt>
     * method, then calling the <code>hashCode</code> method on each of
     * the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     * according to the {@link Object#equals(Object)}
     * method, then calling the <tt>hashCode</tt> method on each of the
     * two objects must produce distinct integer results.  However, the
     * programmer should be aware that producing distinct integer results
     * for unequal objects may improve the performance of hashtables.
     * </ul>
     * <p/>
     * As much as is reasonably practical, the hashCode method defined by
     * class <tt>Object</tt> does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java<font size="-2"><sup>TM</sup></font> programming language.)
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see java.util.Hashtable
     */
    @Override
    public int hashCode() {
        if (getInitiatorState() == null) {
            LOGGER.error("null:initiatorState");
            return 0;

        } else if (getResponderState() == null) {
            LOGGER.error("null:getResponderState");
            return 0;

        } else if (getLinkState() == null) {
            LOGGER.error("null:getLinkState");
            return 0;

        } else {
            if (getLinkState().hashCode() == 0) {
                return getInitiatorState().hashCode() * getResponderState().hashCode();
            }
            return getInitiatorState().hashCode() * getResponderState().hashCode() * getLinkState().hashCode();
        }
    }

    @Override
    public String toString() {
        return "(" + initiator + "," + responder + "," + link + ')';
    }
}
