package org.ppsim.config;

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

    private boolean readInitialNodeState(final String fileName) throws FileNotFoundException {
        BufferedReader bin = new BufferedReader(new FileReader(fileName));
        try {
            String line = bin.readLine();
            while (line != null) {
                if (line.startsWith("Initial_Node_State:")) {
                    line = line.replaceAll("Initial_Node_State:", "");
                    initialNodeState = line;
                    LOGGER.info("Initial_Node_State:" + initialNodeState);
                    return false;
                }
                line = bin.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.error("Initial_Node_State not found!");
        return true;
    }

    private boolean readInitialLinkState(final String fileName) throws FileNotFoundException {
        BufferedReader bin = new BufferedReader(new FileReader(fileName));
        try {
            String line = bin.readLine();
            while (line != null) {
                if (line.startsWith("Initial_Link_State:")) {
                    line = line.replaceAll("Initial_Link_State:", "");
                    initialLinkState = line;
                    LOGGER.info("Initial_Link_State:" + initialLinkState);
                    return false;
                }
                line = bin.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.error("Initial_Link_State not found!");
        return true;
    }

    private boolean readScheduler(final String fileName) throws FileNotFoundException {
        BufferedReader bin = new BufferedReader(new FileReader(fileName));
        try {
            String line = bin.readLine();
            while (line != null) {
                if (line.startsWith("Scheduler:")) {
                    line = line.replaceAll("Scheduler:", "");
                    scheduler = line;
                    LOGGER.info("Scheduler:" + scheduler);
                    return false;
                }
                line = bin.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.error("Scheduler not found!");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
