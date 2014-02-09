/**
 * Computer Engineering and Informatics Department, University of Patras
 *
 * Project:     Undergraduate diploma thesis
 * Created at:  20 Αυγ 2011 1:42:56 μμ
 * Author:      Theofanis Raptis
 *
 */

package ppsim.protocols.uStar;

import ppsim.model.AbstractProtocol;
import ppsim.model.StatePair;

/**
 * Implements the Undirected-Star Protocol.
 */
public class UstarProtocol extends AbstractProtocol<UstarState> {

    /**
     * Define the entries of the transision map.
     */
    protected void setupTransisionsMap() {

        //( (0,(0,0)) , (0,(0,0)) ) -> ( (1,(1, 1)), (2,(0, 0)) )
        addEntry(new StatePair<UstarState>(UstarState.STATE_Q000, UstarState.STATE_Q000),
                new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q200));

        //( (1,(i,j)), (0,(0,0)) ) ->
        //1.    -> ( (1,(0,0)), (2,(i,j+1)) ), if i \in {0,1} and j \in {0,1} or i=2 and j=0
        //2.    -> ( (1,(0,0)), (2,(i,j)) ),   if i=1 and j=2
        //3.    -> (z,z),                      if i=2 and j=1

            //1. if i \in {0,1} and j \in {0,1} or i=2 and j=0
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q201));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q202));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q211));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q212));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q220));

            //2. if i=1 and j=2
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q212));

            //3. if i=2 and j=1
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        //( (2,(l,k)), (0,(0,0)) ) ->
        //1.    -> ( (2,(0,0)), (1,(l+1,k)) ), if k \in {0,1} and l \in {0,1} or k=2 and l=0
        //2.    -> ( (2,(0,0)), (1,(l,k)) ),   if k=1 and l=2
        //3.    -> (z,z),                      if k=2 and l=1

            //1. if k \in {0,1} and l \in {0,1} or k=2 and l=0
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q110));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q201, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q111));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q210, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q120));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q211, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q121));


            addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q102));

            //2. if k=1 and l=2
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q221, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q121));

            //3. if k=2 and l=1
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q212, UstarState.STATE_Q000),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        //( (1,(i,j)), (2,(l,k)) ) ->
        //1.    -> ( (1,(2,j+k)), (2,(0,0)) ),   if j+k<2  and i+l>=2
        //2.    -> ( (1,(i+l,2)), (2,(0,0)) ),   if i+l<2  and j+k>=2
        //3.    -> ( (1,(i+l,j+k)), (2,(0,0)) ), if i+l<2  and j+k<2
        //4.    -> (z,z),                        if i+l>=2 and j+k>=2
        //
        // (x+y>=2 for {x=0,y=2}, {x=1,y=1}, {x=2,y=0}, {x=2,y=2}, {x=1,y=2}, {x=2,y=1})
        // (x+y<2  for {x=0,y=0}, {x=1,y=0}, {x=0,y=1})

            //1. if j+k<2 and i+l>=2    => if ( ({j=0,k=0}, {j=1,k=0}, {j=0,k=1}) && ({i=0,l=2}, {i=1,l=1}, {i=2,l=0}, {i=2,l=2}, {i=1,l=2}, {i=2,l=1})) )

                //1a. j=0, k=0
                    //1a1. i=0, l=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q220),
                            new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q200));

                    //1a2. i=1, l=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q210),
                            new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q200));

                    //1a3. i=2, l=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q200),
                            new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q200));

                    //1a4. i=2, l=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q220),
                            new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q200));

                    //1a5. i=1, l=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q220),
                            new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q200));

                    //1a6. i=2, l=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q210),
                            new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q200));

                //1b. j=1, k=0
                    //1b1. i=0,l=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q220),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

                    //1b2. i=1, l=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q210),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

                    //1b3. i=2, l=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

                    //1a4. i=2, l=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q220),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

                    //1a5. i=1, l=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q220),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

                    //1a6. i=2, l=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q210),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));


                //1c. j=0, k=1
                    //1c1. i=0,l=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q221),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

                    //1c2. i=1, l=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q211),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

                    //1c3. i=2, l=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q201),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

                    //1c4. i=2, l=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q221),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

                    //1c5. i=1, l=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q221),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

                    //1c6. i=2, l=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q211),
                            new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

            //2. if i+l<2 and j+k>=2    => if ( ({i=0,l=0}, {i=1,l=0}, {i=0,l=1}) && ({j=0,k=2}, {j=1,k=1}, {j=2,k=0}, {j=2,k=2}, {j=1,k=2}, {j=2,k=1})) )

                //2a. i=0, l=0

                    //2a1. j=0, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q202),
                            new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q200));

                    //2a2. j=1, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q201),
                            new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q200));

                    //2a3. j=2, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q200),
                            new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q200));

                    //2a4. j=2, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q202),
                            new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q200));

                    //2a5. j=1, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q202),
                            new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q200));

                    //2a6. j=2, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q201),
                            new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q200));

                //2b. i=1, l=0

                    //2b1. j=0, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q202),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                    //2b2. j=1, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q201),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                    //2b3. j=2, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                    //2b4. j=2, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q202),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                    //2b5. j=1, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q202),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                    //2b6. j=2, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q201),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                //2c. i=0, l=1

                    //2c1. j=0, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q212),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                    //2c2. j=1, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q211),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                    //2c3. j=2, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q210),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                    //2c4. j=2, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q212),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                    //2c5. j=1, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q212),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

                    //2c6. j=2, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q211),
                            new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

            //3. if i+l<2 and j+k<2     => if ( ({i=0,l=0}, {i=1,l=0}, {i=0,l=1}) && ({j=0,k=0}, {j=1,k=0}, {j=0,k=1})) )

                //3a. i=0, l=0
                    //j=0, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q200),
                            new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q200));

                    //j=1, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q200),
                            new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q200));

                    //j=0, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q201),
                            new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q200));

                //3b. i=1, l=0
                    //j=0, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q200),
                            new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q200));

                    //j=1, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q200),
                            new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q200));

                    //j=0, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q201),
                            new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q200));

                //3c. i=0, l=1
                    //j=0, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q210),
                            new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q200));

                    //j=1, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q210),
                            new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q200));

                    //j=0, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q211),
                            new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q200));

            //4. if i+l>=2 and j+k>=2   => if ( ({i=0,l=2}, {i=1,l=1}, {i=2,l=0}, {i=2,l=2}, {i=1,l=2}, {i=2,l=1}) && ({j=0,k=2}, {j=1,k=1}, {j=2,k=0}, {j=2,k=2}, {j=1,k=2}, {j=2,k=1})) )

                //4a. i=0, l=2
                    //4a1. j=0, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q222),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4a2. j=1, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q221),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4a3. j=2, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q220),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4a4. j=2, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q222),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4a5. j=1, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q222),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4a6. j=2, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q221),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                //4b. i=1, l=1
                    //4b1. j=0, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q212),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4b2. j=1, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q211),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4b3. j=2, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q210),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4b4. j=2, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q212),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4b5. j=1, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q212),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4b6. j=2, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q211),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                //4c i=2, l=0
                    //4c1. j=0, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q202),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4c2. j=1, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q201),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4c3. j=2, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q200),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4c4. j=2, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q202),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4c5. j=1, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q202),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4c6. j=2, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q201),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));



                //4d. i=2, l=2
                    //4d1. j=0, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q222),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4d2. j=1, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q221),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4d3. j=2, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q220),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4d4. j=2, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q222),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4d5. j=1, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q222),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4d6. j=2, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q221),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                //4e. i=1, l=2
                    //4e1. j=0, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q222),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4e2. j=1, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q221),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4e3. j=2, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q220),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4e4. j=2, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q222),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4e5. j=1, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q222),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4e6. j=2, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q221),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                //4f. i=2, l=1
                    //4f1. j=0, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q212),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4f2. j=1, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q211),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4f3. j=2, k=0
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q210),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4f4. j=2, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q212),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4f5. j=1, k=2
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q212),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

                    //4f6. j=2, k=1
                    addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q211),
                            new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));


        //( (1,(i,j)), (2,(0,0)) ) -> ( (1,(0,0) ), (2,(i,j)) )
        addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q200));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q201));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q202));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q210));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q211));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q212));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q220));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q221));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q222));

        //( (1,(0,0)), (2,(l,k)) ) -> ( (1,(l,k)), (2,(0,0)) )
        addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q200));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q201),
                    new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q200));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q202),
                    new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q200));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q210),
                    new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q200));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q211),
                    new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q200));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q212),
                    new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q200));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q220),
                    new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q200));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q221),
                    new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q200));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q222),
                    new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q200));

        //( (1,(i,j)), (1,(l,k)) ) -> (z,z)

            //1. ( (1,(0,0)), (1,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q100),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q101),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q102),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q110),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q111),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q112),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q120),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q121),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q100, UstarState.STATE_Q122),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //2. ( (1,(0,1)), (1,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q100),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q101),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q102),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q110),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q111),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q112),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q120),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q121),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q101, UstarState.STATE_Q122),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //3. ( (1,(0,2)), (1,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q100),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q101),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q102),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q110),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q111),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q112),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q120),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q121),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q102, UstarState.STATE_Q122),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //4. ( (1,(1,0)), (1,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q100),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q101),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q102),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q110),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q111),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q112),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q120),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q121),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q110, UstarState.STATE_Q122),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //5. ( (1,(1,1)), (1,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q100),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q101),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q102),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q110),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q111),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q112),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q120),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q121),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q111, UstarState.STATE_Q122),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //6. ( (1,(1,2)), (1,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q100),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q101),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q102),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q110),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q111),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q112),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q120),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q121),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q112, UstarState.STATE_Q122),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //7. ( (1,(2,0)), (1,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q100),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q101),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q102),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q110),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q111),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q112),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q120),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q121),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q120, UstarState.STATE_Q122),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //8. ( (1,(2,1)), (1,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q100),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q101),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q102),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q110),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q111),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q112),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q120),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q121),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q121, UstarState.STATE_Q122),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //9. ( (1,(2,2)), (1,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q100),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q101),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q102),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q110),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q111),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q112),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q120),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q121),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q122, UstarState.STATE_Q122),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        //( (2,(i,j)), (2,(l,k)) ) -> (z,z)
            
            //1. ( (2,(0,0)), (2,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q201),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q202),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q210),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q211),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q212),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q220),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q221),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q200, UstarState.STATE_Q222),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //2. ( (2,(0,1)), (2,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q201, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q201, UstarState.STATE_Q201),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q201, UstarState.STATE_Q202),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q201, UstarState.STATE_Q210),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q201, UstarState.STATE_Q211),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q201, UstarState.STATE_Q212),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q201, UstarState.STATE_Q220),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q201, UstarState.STATE_Q221),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q201, UstarState.STATE_Q222),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //3. ( (2,(0,2)), (2,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q202, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q202, UstarState.STATE_Q201),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q202, UstarState.STATE_Q202),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q202, UstarState.STATE_Q210),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q202, UstarState.STATE_Q211),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q202, UstarState.STATE_Q212),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q202, UstarState.STATE_Q220),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q202, UstarState.STATE_Q221),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q202, UstarState.STATE_Q222),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //4. ( (2,(1,0)), (2,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q210, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q210, UstarState.STATE_Q201),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q210, UstarState.STATE_Q202),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q210, UstarState.STATE_Q210),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q210, UstarState.STATE_Q211),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q210, UstarState.STATE_Q212),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q210, UstarState.STATE_Q220),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q210, UstarState.STATE_Q221),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q210, UstarState.STATE_Q222),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //5. ( (2,(1,1)), (2,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q211, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q211, UstarState.STATE_Q201),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q211, UstarState.STATE_Q202),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q211, UstarState.STATE_Q210),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q211, UstarState.STATE_Q211),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q211, UstarState.STATE_Q212),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q211, UstarState.STATE_Q220),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q211, UstarState.STATE_Q221),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q211, UstarState.STATE_Q222),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //6. ( (2,(1,2)), (2,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q212, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q212, UstarState.STATE_Q201),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q212, UstarState.STATE_Q202),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q212, UstarState.STATE_Q210),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q212, UstarState.STATE_Q211),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q212, UstarState.STATE_Q212),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q212, UstarState.STATE_Q220),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q212, UstarState.STATE_Q221),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q212, UstarState.STATE_Q222),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //7. ( (2,(2,0)), (2,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q220, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q220, UstarState.STATE_Q201),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q220, UstarState.STATE_Q202),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q220, UstarState.STATE_Q210),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q220, UstarState.STATE_Q211),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q220, UstarState.STATE_Q212),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q220, UstarState.STATE_Q220),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q220, UstarState.STATE_Q221),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q220, UstarState.STATE_Q222),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //8. ( (2,(2,1)), (2,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q221, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q221, UstarState.STATE_Q201),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q221, UstarState.STATE_Q202),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q221, UstarState.STATE_Q210),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q221, UstarState.STATE_Q211),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q221, UstarState.STATE_Q212),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q221, UstarState.STATE_Q220),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q221, UstarState.STATE_Q221),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q221, UstarState.STATE_Q222),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            //9. ( (2,(2,2)), (2,(l,k)) ) -> (z,z)
            addEntry(new StatePair<UstarState>(UstarState.STATE_Q222, UstarState.STATE_Q200),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q222, UstarState.STATE_Q201),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q222, UstarState.STATE_Q202),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q222, UstarState.STATE_Q210),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q222, UstarState.STATE_Q211),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q222, UstarState.STATE_Q212),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q222, UstarState.STATE_Q220),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q222, UstarState.STATE_Q221),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

            addEntry(new StatePair<UstarState>(UstarState.STATE_Q222, UstarState.STATE_Q222),
                    new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        //(z,x) -> (z,z)
        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q000),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q001),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q002),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q010),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q011),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q012),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q020),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q021),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q022),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q100),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q101),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q102),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q110),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q111),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q112),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q120),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q121),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q122),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q200),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q201),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q202),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q210),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q211),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q212),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q220),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q221),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Q222),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));

        addEntry(new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z),
                new StatePair<UstarState>(UstarState.STATE_Z, UstarState.STATE_Z));
    }
}