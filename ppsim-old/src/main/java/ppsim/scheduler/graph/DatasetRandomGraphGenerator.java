/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  14 Οκτ 2011 1:51:12 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.scheduler.graph;

import java.util.*;
import java.io.*;

/**
 * Generates a realistic random graph that models the real scenario from a given dataset.
 */
public class DatasetRandomGraphGenerator {

   /**
     * Connectivity threshold for G_np graphs.
     */
    public static final double CONNECTIVITY_THR = 1.05;


    public void generate(final GraphScheduler scheduler) throws FileNotFoundException {
        final int totAgents = scheduler.size();
        final Random randomGenerator = new Random();
        final double edgeProbability = CONNECTIVITY_THR * java.lang.Math.log(totAgents) / totAgents;

        //array with the probabilities of exixtence of each edge
        double probability[][] = new double[totAgents][totAgents];
        for(int i=0;i<totAgents;i++){
            for(int j=0;j<totAgents;j++) probability[i][j] = 0;
        }

        List<Integer> l1 = new ArrayList<Integer>();
        List<Integer> l2 = new ArrayList<Integer>();
        Scanner s = new Scanner(new FileReader("C:/dataset.txt"));
        while (s.hasNext()) {
            l1.add(s.nextInt());
            l2.add(s.nextInt());
        }
        s.close();

        //define probabilities of edge existence
        int interactions = l1.size();
        int initiator;
        int responder;

        int k = 0;
	while (k < interactions) {
            initiator = l1.get(k);
            responder = l2.get(k);
            probability[initiator][responder]++;
            k++;
	}
        for(int i=0;i<totAgents;i++){
            for(int j=0;j<totAgents;j++) {
                probability[i][j] /= interactions;
                //check for connectivity
                if (probability[i][j] == 0.0000) probability[i][j]=edgeProbability;
            }
        }

        // Iterate through all agents acting as initiators
        for (int i = 0; i < totAgents; i++) {
            // Iterate through all agents acting as responders
            for (int j = 0; j < totAgents; j++) {
                //if (i == j) {
               //     continue;
               // }
                // Sample Edge
                if (probability[i][j] >= randomGenerator.nextDouble()) scheduler.setupEdge(i, j);
            }
        }
    }
}
