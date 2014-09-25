package org.netcs.web;

import org.netcs.Experiment;
import org.netcs.ExperimentExecutor;
import org.netcs.model.population.PopulationLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class ExperimentController {

    @Autowired
    ExperimentExecutor experimentExecutor;

    @PostConstruct
    public void init() throws Exception {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    experimentExecutor.start(new String[]{"global-line3.prop", "global-line3.out", "10", "5"});
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();


    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(final Map<String, Object> model) {
        return "redirect:/experiment";
    }

    @RequestMapping(value = "/experiment/add", method = RequestMethod.GET)
    public String addNewExperiment(final Map<String, Object> model) {
        model.put("title", "Add Experiment");
        return "experiment/add";
    }

    @RequestMapping(value = "/experiment/add", method = RequestMethod.POST)
    public String addNewExperimentPost(final Map<String, Object> model) {
        return "add";
    }

    @RequestMapping(value = "/experiment", method = RequestMethod.GET)
    public String experiment(final Map<String, Object> model) {
        return "redirect:/experiment/0";
    }

    @RequestMapping(value = "/experiment/{experimentId}", method = RequestMethod.GET)
    public String viewExperiment(final Map<String, Object> model, @PathVariable("experimentId") int experimentId) {

        Experiment experiment = experimentExecutor.getExperiment(experimentId);

        System.out.println("experiment:" + experiment.getExperiment().getPopulation().getNodes().size() + "nodes");
        model.put("population", experiment.getExperiment().getPopulation());
        model.put("nodes", experiment.getExperiment().getPopulation().getNodes());
        ArrayList<String> edges = new ArrayList<>();
        Iterator<PopulationLink<String>> edgesIterator = experiment.getExperiment().getPopulation().getEdges().iterator();
        while (edgesIterator.hasNext()) {
            PopulationLink<String> currentEdge = edgesIterator.next();
            if (currentEdge.getState().equals("1")) {
                edges.add(currentEdge.getDefaultEdge().toString());
            }
        }

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader("exp.out"));
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                sb.append(line).append("\n");
            }
            br.close();
        } catch (Exception e) {

        }
        model.put("logs", sb.toString());
        model.put("edges", edges);
        model.put("experimentId", experimentId);
        return "experiment/view";
    }
}
