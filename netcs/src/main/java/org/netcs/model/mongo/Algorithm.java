package org.netcs.model.mongo;


import org.netcs.config.ConfigFile;
import org.netcs.config.Transition;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Algorithm {

    @Id
    private String id;

    private String name;

    private List<Transition> transitions;

    private ConfigFile configFile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public void setConfigFile(ConfigFile configFile) {
        this.configFile = configFile;
    }

    @Override
    public String toString() {
        return name;
    }
}