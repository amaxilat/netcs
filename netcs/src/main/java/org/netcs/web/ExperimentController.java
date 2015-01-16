package org.netcs.web;

import org.apache.log4j.Logger;
import org.netcs.AdvancedRunnableExperiment;
import org.netcs.ExperimentExecutor;
import org.netcs.RunnableExperiment;
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
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class ExperimentController {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(ExperimentController.class);

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

    @RequestMapping(value = "/experiment", method = RequestMethod.GET)
    public String listExperiments(final Map<String, Object> model) {
        model.put("title", "Experiments");
        SortedSet<RunnableExperiment> experimentSet = new TreeSet<>(new Comparator<RunnableExperiment>() {
            @Override
            public int compare(RunnableExperiment o1, RunnableExperiment o2) {
                return (int) (o1.getIndex() - o2.getIndex());
            }
        });
        experimentSet.addAll(experimentExecutor.getExperiments());

        SortedSet<AdvancedRunnableExperiment> advancedExperimentSet = new TreeSet<>(new Comparator<AdvancedRunnableExperiment>() {
            @Override
            public int compare(AdvancedRunnableExperiment o1, AdvancedRunnableExperiment o2) {
                return (int) (o1.getIndex() - o2.getIndex());
            }
        });
        advancedExperimentSet.addAll(experimentExecutor.getAdvancedExperiments());
        model.put("experiments", experimentSet);
        model.put("advancedExperiments", advancedExperimentSet);
        return "experiment/list";
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

    @RequestMapping(value = "/experiment/{experimentId}", method = RequestMethod.GET)
    public String viewExperiment(final Map<String, Object> model, @PathVariable("experimentId") int experimentId) {
        model.put("title", experimentId);

        RunnableExperiment experiment = experimentExecutor.getExperiment(experimentId);

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
            LOGGER.error(e, e);
        }
        model.put("logs", sb.toString());
        model.put("edges", edges);
        model.put("experiment", experiment);
        model.put("experimentId", experimentId);
        return "experiment/view";
    }
}
