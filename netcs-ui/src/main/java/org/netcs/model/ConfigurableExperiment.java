package org.netcs.model;


import org.netcs.service.LookupService;
import org.netcs.config.ConfigFile;
import org.netcs.scheduler.AbstractScheduler;

public class ConfigurableExperiment extends AbstractExperiment<String, AbstractProtocol> {

    /**
     * Default constructor.
     *
     * @param configFile the configuration file to use.
     * @param scheduler  the scheduler to use.
     * @param index
     */
    public ConfigurableExperiment(ConfigFile configFile, final AbstractProtocol protocol, AbstractScheduler scheduler, long index,final LookupService lookupService) {
        super(configFile, protocol, scheduler, index,lookupService);
    }

    @Override
    protected void completeExperiment() {
//        StringBuilder resultString = new StringBuilder();
//
//        resultString.append("ExperimentEnded after ").append(getInteractions()).append(" interactions.\n");
//        resultString.append("NodeStatuses:").append("\n");
//        for (PopulationNode node : getPopulation().getNodes()) {
//            resultString.append(node.getNodeName()).append(":").append(node.getState()).append("\n");
//        }
//        resultString.append("LinkStatuses:").append("\n");
//        for (PopulationLink edge : getPopulation().getEdges()) {
//            if (edge.getState().equals("1")) {
//                resultString.append(edge.getDefaultEdge()).append(":").append(edge.getState()).append("\n");
//            }
//        }
//        this.resultString = resultString.toString();
//        LOGGER.debug(this.resultString);
    }

}
