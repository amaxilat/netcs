/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  28 Αυγ 2011 3:37:44 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.protocols.twoCycle;

import ppsim.model.AbstractProtocol;
import ppsim.model.StatePair;

/**
 * Implements the TwoCycle Protocol.
 */
public class TwoCycleProtocol extends AbstractProtocol<TwoCycleState>  {

    /**
     * Define the entries of the transision map.
     */
    protected void setupTransisionsMap() {

        //( (l,*),(l,*) ) -> ( (l,s),(f,w) )
        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_LS),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_LC),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_LD),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LC, TwoCycleState.STATE_LS),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LD, TwoCycleState.STATE_LS),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW));
	//		 
        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LC, TwoCycleState.STATE_LC),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LC, TwoCycleState.STATE_LD),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LD, TwoCycleState.STATE_LD),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW));			 

        //( (l,s),(f,w) ) -> ( (f,p),(l,c) )
        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_FP, TwoCycleState.STATE_LC));

        //( (l,c),(f,p) ) -> ( (l,d),(f,p) )
        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LC, TwoCycleState.STATE_FP),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LD, TwoCycleState.STATE_FP));

        //( (f,{p,d}),(l,s) ) -> ( (l,s),(f,w) )
        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_FP, TwoCycleState.STATE_LS),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_FD, TwoCycleState.STATE_LS),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FW));

        //( (l,s),(f,{p,d}) ) -> ( (f,w),(l,s) )
        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FP),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_FW, TwoCycleState.STATE_LS));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LS, TwoCycleState.STATE_FD),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_FW, TwoCycleState.STATE_LS));
        
        //( (l,d),(f,*) ) -> ( (f,d),(l,d) )
        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LD, TwoCycleState.STATE_FW),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_FD, TwoCycleState.STATE_LD));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LD, TwoCycleState.STATE_FP),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_FD, TwoCycleState.STATE_LD));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_LD, TwoCycleState.STATE_FD),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_FD, TwoCycleState.STATE_LD));

        //( (f,*),(l,d) ) -> ( (l,d),(f,d) )
        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_FW, TwoCycleState.STATE_LD),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LD, TwoCycleState.STATE_FD));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_FP, TwoCycleState.STATE_LD),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LD, TwoCycleState.STATE_FD));

        addEntry(new StatePair<TwoCycleState>(TwoCycleState.STATE_FD, TwoCycleState.STATE_LD),
                new StatePair<TwoCycleState>(TwoCycleState.STATE_LD, TwoCycleState.STATE_FD));
    }
}
