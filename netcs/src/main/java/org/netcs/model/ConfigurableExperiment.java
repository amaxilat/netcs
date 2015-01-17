package org.netcs.model;


import org.netcs.LookupService;
import org.netcs.config.ConfigFile;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.population.PopulationNode;
import org.netcs.scheduler.AbstractScheduler;

public class ConfigurableExperiment extends AbstractExperiment<String, AbstractProtocol<String>> {

    /**
     * Default constructor.
     *
     * @param configFile the configuration file to use.
     * @param scheduler  the scheduler to use.
     * @param index
     */
    public ConfigurableExperiment(ConfigFile configFile, final AbstractProtocol<String> protocol, AbstractScheduler<String> scheduler, long index,final LookupService lookupService) {
        super(configFile, protocol, scheduler, index,lookupService);
    }

    @Override
    protected void completeExperiment() {
        StringBuilder resultString = new StringBuilder();

        resultString.append("ExperimentEnded after ").append(getInteractions()).append(" interactions.\n");
        resultString.append("NodeStatuses:").append("\n");
        for (PopulationNode<String> node : getPopulation().getNodes()) {
            resultString.append(node.getNodeName()).append(":").append(node.getState()).append("\n");
        }
        resultString.append("LinkStatuses:").append("\n");
        for (PopulationLink<String> edge : getPopulation().getEdges()) {
            if (edge.getState().equals("1")) {
                resultString.append(edge.getDefaultEdge()).append(":").append(edge.getState()).append("\n");
            }
        }
        this.resultString = resultString.toString();
        LOGGER.debug(this.resultString);
    }

}
