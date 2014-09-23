package org.netcs.web;

import org.netcs.ExperimentExecutor;
import org.netcs.model.population.PopulationLink;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class ExperimentController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(final Map<String, Object> model) {
        return "redirect:/experiment";
    }

    @RequestMapping(value = "/experiment", method = RequestMethod.GET)
    public String experiment(final Map<String, Object> model) {
        return "redirect:/experiment/0";
    }

    @RequestMapping(value = "/experiment/{experimentId}", method = RequestMethod.GET)
    public String viewExperiment(final Map<String, Object> model, @PathVariable("experimentId") int experimentId) {
        ExperimentExecutor.ConfigurableExperiment experiment = ExperimentExecutor.getInstance().getExperiment(experimentId);

        System.out.println("experiment:" + experiment.getPopulation().getNodes().size() + "nodes");
        model.put("population", experiment.getPopulation());
        model.put("nodes", experiment.getPopulation().getNodes());
        ArrayList<String> edges = new ArrayList<>();
        Iterator<PopulationLink<String>> edgesIterator = experiment.getPopulation().getEdges().iterator();
        while (edgesIterator.hasNext()) {
            PopulationLink<String> currentEdge = edgesIterator.next();
            if (currentEdge.getState().equals("1")) {
                edges.add(currentEdge.getDefaultEdge().toString());
            }
        }
        model.put("edges", edges);
        model.put("experimentId", experimentId);
        return "graph";
    }
}
