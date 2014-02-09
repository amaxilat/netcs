/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  19 Αυγ 2011 8:44:44 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.protocols.inDegreeGreaterThanOne;

import ppsim.model.AbstractAgent;
import ppsim.model.Population;

/**
 * Implements the agent of the In-Degree-Greater-Than-1 protocol.
 */
public class IdgtOneAgent extends AbstractAgent<IdgtOneState> {

    /**
     * Default constructor.
     *
     * @param pop the Population holder.
     */
    public IdgtOneAgent(final Population<IdgtOneState> pop) {
        super(pop);
    }

    /**
     * Initializes the state of the agent.
     */
    public void initAgent() {
        setState(IdgtOneState.STATE_INIT);
    }
}
