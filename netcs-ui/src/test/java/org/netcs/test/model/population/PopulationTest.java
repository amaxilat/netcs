package org.netcs.test.model.population;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.netcs.model.population.MemoryPopulation;
import org.netcs.model.population.Population;
import org.netcs.model.population.PopulationLink;

import java.util.Collection;

/**
 * Tests operations on Population.
 */
public class PopulationTest {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(PopulationTest.class);


    @Test
    public void testPopulation() {
        final Population population = new MemoryPopulation();
        Assert.assertEquals(population.size(), 0);
    }

    @Test
    public void testPopulationWithSize() {
        final int populationSize = 10;
        final Population population = new MemoryPopulation(populationSize);
        Assert.assertEquals(population.size(), populationSize);
    }

    @Test
    public void testPopulationEdges() {
        final int populationSize = 100;
        final Population population = new MemoryPopulation(populationSize);
        Collection<PopulationLink> edges = population.getEdges();
        int expectedEdgeCount = 0;
        for (int j = populationSize - 1; j > 0; j--) {
            expectedEdgeCount += j;
        }
        Assert.assertEquals(edges.size(), expectedEdgeCount);
    }

}
