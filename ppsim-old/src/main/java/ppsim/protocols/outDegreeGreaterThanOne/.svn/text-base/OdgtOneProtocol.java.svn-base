/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  28 Αυγ 2011 5:06:27 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.protocols.outDegreeGreaterThanOne;

import ppsim.model.AbstractProtocol;
import ppsim.model.StatePair;

/**
 * Implements the Out-Degree-Greater-Than-1 Protocol.
 */
public class OdgtOneProtocol extends AbstractProtocol<OdgtOneState>  {

    /**
     * Define the entries of the transision map.
     */
    protected void setupTransisionsMap() {

        //1. (-,-)->(I,R)
        addEntry(new StatePair<OdgtOneState>(OdgtOneState.STATE_INIT, OdgtOneState.STATE_INIT),
        new StatePair<OdgtOneState>(OdgtOneState.STATE_I, OdgtOneState.STATE_R));

        //2. (I,R)->(-,-)
        addEntry(new StatePair<OdgtOneState>(OdgtOneState.STATE_I, OdgtOneState.STATE_R),
        new StatePair<OdgtOneState>(OdgtOneState.STATE_INIT, OdgtOneState.STATE_INIT));

        //3. (I,-)->(Y,Y)
        addEntry(new StatePair<OdgtOneState>(OdgtOneState.STATE_I, OdgtOneState.STATE_INIT),
        new StatePair<OdgtOneState>(OdgtOneState.STATE_Y, OdgtOneState.STATE_Y));

        //4a. (Y,-)->(Y,Y)
        addEntry(new StatePair<OdgtOneState>(OdgtOneState.STATE_Y, OdgtOneState.STATE_INIT),
        new StatePair<OdgtOneState>(OdgtOneState.STATE_Y, OdgtOneState.STATE_Y));

        //4b. (Y,R)->(Y,Y)
        addEntry(new StatePair<OdgtOneState>(OdgtOneState.STATE_Y, OdgtOneState.STATE_R),
        new StatePair<OdgtOneState>(OdgtOneState.STATE_Y, OdgtOneState.STATE_Y));

        //5a. (-,Y)->(Y,Y)
        addEntry(new StatePair<OdgtOneState>(OdgtOneState.STATE_INIT, OdgtOneState.STATE_Y),
        new StatePair<OdgtOneState>(OdgtOneState.STATE_Y, OdgtOneState.STATE_Y));

        //5a. (I,Y)->(Y,Y)
        addEntry(new StatePair<OdgtOneState>(OdgtOneState.STATE_I, OdgtOneState.STATE_Y),
        new StatePair<OdgtOneState>(OdgtOneState.STATE_Y, OdgtOneState.STATE_Y));
    }
}