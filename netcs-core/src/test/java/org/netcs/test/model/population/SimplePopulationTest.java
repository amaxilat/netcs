package org.netcs.test.model.population;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.netcs.model.population.Population;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;
import org.netcs.model.population.SimplePopulation;

import java.util.Collection;

/**
 * Tests operations on Population.
 */
public class SimplePopulationTest {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(SimplePopulationTest.class);


    @Test
    public void testPopulation() {
        final Population population = new SimplePopulation();
        Assert.assertEquals(0, population.size());
    }

    @Test
    public void testPopulationWithSize() {
        final int populationSize = 10;
        final Population population = new SimplePopulation(populationSize);
        Assert.assertEquals(populationSize, population.size());
    }

    @Test
    public void testPopulationEdges() {
        final int populationSize = 100;
        final Population population = new SimplePopulation(populationSize);
        Collection<PopulationLink> edges = population.getEdges();
        int expectedEdgeCount = 0;
        for (int j = populationSize - 1; j > 0; j--) {
            expectedEdgeCount += j;
        }
        Assert.assertEquals(expectedEdgeCount, edges.size());
    }

    @Test
    public void testPopulationEdgesGet() {
        final int populationSize = 100;
        final Population population = new SimplePopulation(populationSize);
        for (PopulationNode start : population.getNodes()) {
            for (PopulationNode end : population.getNodes()) {
                if (!start.equals(end)) {
                    System.out.println(start + "--" + end);
                    Assert.assertNotNull(population.getEdge(start, end));
                }
            }
        }
    }

    @Test
    public void testPopulationNodesActual() {
        final int populationSize = 100;
        final Population population = new SimplePopulation(populationSize);
        for (PopulationNode node : population.getNodes()) {
            population.getActualDegree(node);
        }
    }

}
