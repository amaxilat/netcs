/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  28 Αυγ 2011 2:06:29 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph;

/**
 * Generates Undirected Bipartite Graphs.
 */
public class BiGraphUndirGenerator {

    /**
     * Graph generator.
     *
     * @param scheduler the Graph Scheduler that stores the graph properties.
     * @param directed  true if the graph has directed edges, otherwise false.
     
    public void generate(final GraphScheduler scheduler) {
        Random generator = new Random();
        final int totAgents = scheduler.size();

        //random definition of the two bigraph disjoint sets
        //agents with id<split belong to the first set
        //agents with id>=split belong to the second set
        int split = generator.nextInt(totAgents);

        //random definition of connection between agents of different sets
        for(int i=0; i<split; i++){
            for(int j=split; j<totAgents; j++){
                double coin = Math.random();
                if (coin < 0.5){
                    scheduler.setupEdge(i, j);
                    scheduler.setupEdge(j, i);
                }
            }
        }
    }
*/


    /**
     * BiGraph generator for fet dataset experiment *ONLY*.
     *
     * @param scheduler the Graph Scheduler that stores the graph properties.
     */

    
      public void generate(final GraphScheduler scheduler) {
           final int n1 = 36;
           final int n2 = 68;
           final int totAgents = 104;

           for(int i=0; i<n1; i++){
               for(int j=n1; j<totAgents; j++){
                   scheduler.setupEdge(i, j);
                   scheduler.setupEdge(j, i);
               }
            }   
      }
    
}
