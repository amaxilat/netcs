package org.netcs.config;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes the configuration settings for an algorithm experiment.
 */
public class ConfigFile {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(ConfigFile.class);

    /**
     * The transitions defined in the ConfigFile.
     */
    private final List<Transition> transitions;
    /**
     * The initial State for each node of the network.
     */
    private String initialNodeState;
    /**
     * The initial State for each link of the network.
     */
    private String initialLinkState;
    /**
     * The name of the Scheduler to be used.
     */
    private String scheduler;
    /**
     * The size of the population to experiment with.
     */
    private long populationSize;
    /**
     * The number of experiments to execute to generate mean values.
     */
    private long iterations;
    /**
     * Debug Level on the outputFile.
     */
    private int debugLevel;

    /**
     * Creates a ConfigFile object based on the file provided.
     *
     * @param fileName the name of the config file to load.
     * @throws FileNotFoundException
     */
    public ConfigFile(final String fileName) throws FileNotFoundException {
        transitions = new ArrayList<>();

        if (readInitialNodeState(fileName)) {
            LOGGER.error("Could not locate Initial_Node_State");
        }
        if (readInitialLinkState(fileName)) {
            LOGGER.error("Could not locate Initial_Link_State");
        }
        if (readScheduler(fileName)) {
            LOGGER.error("Could not locate Scheduler");
        }
        if (readPopulationSize(fileName)) {
            LOGGER.error("Could not locate PopulationSize");
        }
        if (readIterations(fileName)) {
            LOGGER.error("Could not locate Iterations");
        }
        if (readDebugLevel(fileName)) {
            LOGGER.error("Could not locate DebugLevel");
        }
        readTransitionMap(fileName);
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public String getInitialNodeState() {
        return initialNodeState;
    }

    public String getInitialLinkState() {
        return initialLinkState;
    }

    public String getScheduler() {
        return scheduler;
    }

    private boolean readDebugLevel(final String fileName) throws FileNotFoundException {
        String retVal = readProperty(fileName, "Debug_Level");
        if (retVal != null) {
            debugLevel = Integer.parseInt(retVal);
            return false;
        }
        return true;
    }


    private boolean readInitialNodeState(final String fileName) throws FileNotFoundException {
        String retVal = readProperty(fileName, "Initial_Node_State");
        if (retVal != null) {
            initialNodeState = retVal;
            return false;
        }
        return true;
    }

    private boolean readInitialLinkState(final String fileName) throws FileNotFoundException {
        String retVal = readProperty(fileName, "Initial_Link_State");
        if (retVal != null) {
            initialLinkState = retVal;
            return false;
        }
        return true;
    }

    private boolean readScheduler(final String fileName) throws FileNotFoundException {
        String retVal = readProperty(fileName, "Scheduler");
        if (retVal != null) {
            scheduler = retVal;
            return false;
        }
        return true;
    }

    private boolean readIterations(final String fileName) throws FileNotFoundException {
        String retVal = readProperty(fileName, "Iterations");
        if (retVal != null) {
            iterations = Long.parseLong(retVal);
            return false;
        }
        return true;
    }

    private boolean readPopulationSize(final String fileName) throws FileNotFoundException {
        String retVal = readProperty(fileName, "PopulationSize");
        if (retVal != null) {
            populationSize = Long.parseLong(retVal);
            return false;
        }
        return true;
    }

    private void readTransitionMap(final String fileName) throws FileNotFoundException {
        BufferedReader bin = new BufferedReader(new FileReader(fileName));
        boolean inTransitionMap = false;
        try {
            String line = bin.readLine();
            while (line != null) {
                if (line.equals("Transition_function:")) {
                    inTransitionMap = true;
                } else if (inTransitionMap && !line.startsWith("#")) {
                    line = line.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "");
                    final String[] parts = line.split(",");
                    Transition newTransition = new Transition(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    LOGGER.info(newTransition.toString());
                    transitions.add(newTransition);
                }
                line = bin.readLine();
            }
            LOGGER.info("Read " + transitions.size() + " transitions.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readProperty(final String fileName, final String propertyName) throws FileNotFoundException {
        BufferedReader bin = new BufferedReader(new FileReader(fileName));
        try {
            String line = bin.readLine();
            while (line != null) {
                if (line.startsWith(propertyName + ":")) {
                    line = line.replaceAll(propertyName + ":", "");
                    LOGGER.info(propertyName + ":" + line);
                    return line;
                }
                line = bin.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.error(propertyName + " not found!");
        return null;
    }

    public long getPopulationSize() {
        return populationSize;
    }

    public long getIterations() {
        return iterations;
    }

    public void setPopulationSize(Long populationSize) {
        this.populationSize = populationSize;
    }

    public int getDebugLevel() {
        return debugLevel;
    }
}
