/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  27 Σεπ 2011 5:56:43 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler;

import ppsim.model.AbstractAgent;
import java.util.*;

/**
 * Implements scheduler based on a 2-column dataset file.
 *
 * @param <State> the variable type for the state of the agent.
 */
public class DatasetScheduler<State> extends AbstractScheduler<State>  {

    public void interact(){

    }

    
    /**
     * Performs an interaction between an initiator agent and a responder agent.
     * The scheduler selects the initiator from the first list and
     * the responder from the second list.
     */
    public void interact(List<Integer> l1, List<Integer> l2, long round) {

        int roundNotLong = (int)round;

        final int init = l1.get(roundNotLong);
        final int resp = l2.get(roundNotLong);

        final AbstractAgent<State> initAgent = getAgent(init);
        final AbstractAgent<State> respAgent = getAgent(resp);

        // Conduct interaction for given pair of agents
        interact(initAgent, respAgent);
    }


    /*
     * converts file to list

    public void fileToList() {
        try {
            List<Integer> l1 = new ArrayList<Integer>();
            List<Integer> l2 = new ArrayList<Integer>();
            Scanner s = new Scanner(new FileReader("C:/dataset.txt"));
            while (s.hasNext()) {
                l1.add(s.nextInt());
                l2.add(s.nextInt());
            }
            s.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatasetScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
