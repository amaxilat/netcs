/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  19 Αυγ 2011 8:45:07 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.protocols.inDegreeGreaterThanOne;

import ppsim.model.AbstractProtocol;
import ppsim.model.StatePair;

/**
 * Implements the In-Degree-Greater-Than-1 Protocol.
 */
public class IdgtOneProtocol extends AbstractProtocol<IdgtOneState> {

    /**
     * Define the entries of the transision map.
     */
    protected void setupTransisionsMap() {

        //1. (-,-)->(I,R)
        addEntry(new StatePair<IdgtOneState>(IdgtOneState.STATE_INIT, IdgtOneState.STATE_INIT),
        new StatePair<IdgtOneState>(IdgtOneState.STATE_I, IdgtOneState.STATE_R));

        //2. (I,R)->(-,-)
        addEntry(new StatePair<IdgtOneState>(IdgtOneState.STATE_I, IdgtOneState.STATE_R),
        new StatePair<IdgtOneState>(IdgtOneState.STATE_INIT, IdgtOneState.STATE_INIT));

        //3. (-,R)->(Y,Y)
        addEntry(new StatePair<IdgtOneState>(IdgtOneState.STATE_INIT, IdgtOneState.STATE_R),
        new StatePair<IdgtOneState>(IdgtOneState.STATE_Y, IdgtOneState.STATE_Y));
    
        //4a. (Y,-)->(Y,Y)
        addEntry(new StatePair<IdgtOneState>(IdgtOneState.STATE_Y, IdgtOneState.STATE_INIT),
        new StatePair<IdgtOneState>(IdgtOneState.STATE_Y, IdgtOneState.STATE_Y));
        
        //4b. (Y,R)->(Y,Y)
        addEntry(new StatePair<IdgtOneState>(IdgtOneState.STATE_Y, IdgtOneState.STATE_R),
        new StatePair<IdgtOneState>(IdgtOneState.STATE_Y, IdgtOneState.STATE_Y));
        
        //5a. (-,Y)->(Y,Y)
        addEntry(new StatePair<IdgtOneState>(IdgtOneState.STATE_INIT, IdgtOneState.STATE_Y),
        new StatePair<IdgtOneState>(IdgtOneState.STATE_Y, IdgtOneState.STATE_Y));
        
        //5a. (I,Y)->(Y,Y)
        addEntry(new StatePair<IdgtOneState>(IdgtOneState.STATE_I, IdgtOneState.STATE_Y),
        new StatePair<IdgtOneState>(IdgtOneState.STATE_Y, IdgtOneState.STATE_Y));
    }
}
