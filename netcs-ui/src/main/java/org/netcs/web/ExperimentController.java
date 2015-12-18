package org.netcs.web;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;
import org.netcs.AdvancedRunnableExperiment;
import org.netcs.RunnableExperiment;
import org.netcs.component.ExperimentExecutor;
import org.netcs.model.population.PopulationLink;
import org.netcs.model.sql.Experiment;
import org.netcs.model.sql.ExperimentRepository;
import org.netcs.model.sql.TerminationStat;
import org.netcs.model.sql.TerminationStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class ExperimentController extends BaseController {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(ExperimentController.class);


    @Autowired
    ExperimentExecutor experimentExecutor;
    @Autowired
    ExperimentRepository experimentRepository;
    @Autowired
    TerminationStatRepository terminationStatRepository;

    @PostConstruct
    public void init() {
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
        populateAlgorithms(model);

        mixPanelService.log(getUser(), "experiment", "view", "all");

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
//        TODO:
//        advancedExperimentSet.addAll(experimentExecutor.getAdvancedExperiments());
        model.put("experiments", experimentSet);
        model.put("advancedExperiments", advancedExperimentSet);
        return "experiment/list";
    }

    @RequestMapping(value = "/api/experiment/max", method = RequestMethod.GET)
    public String maxExperiments(@RequestParam("size") final int size) {
        experimentExecutor.MAX_CONCURENT_EXPERIMENTS = size;
        return "redirect:/";
    }

    @RequestMapping(value = "/api/experiment/algorithm", method = RequestMethod.GET)
    public String mineSimpleName(@RequestParam("algorithm") final String algorithm) {
        experimentExecutor.count = 100L;
        experimentExecutor.mineSimpleName = algorithm;
        return "redirect:/";
    }

    @RequestMapping(value = "/api/experiment/scheduler", method = RequestMethod.GET)
    public String mineSimpleScheduler(@RequestParam("scheduler") final String scheduler) {
        experimentExecutor.count = 100L;
        experimentExecutor.mineSimpleScheduler = scheduler;
        return "redirect:/";
    }

    @RequestMapping(value = "/api/experiment/limit", method = RequestMethod.GET)
    public String mineSimpleScheduler(@RequestParam("limit") final int limit) {
        experimentExecutor.count = 100L;
        experimentExecutor.mineSimpleThreshold = limit;
        return "redirect:/";
    }


    @RequestMapping(value = "/experiment/add", method = RequestMethod.GET)
    public String addNewExperiment(final Map<String, Object> model) {
        populateAlgorithms(model);
        model.put("title", "Add Experiment");
        return "experiment/add";
    }

    @RequestMapping(value = "/experiment/add", method = RequestMethod.POST)
    public String addNewExperimentPost(final Map<String, Object> model) {
        return "add";
    }

    @RequestMapping(value = "/experiment/{experimentId}", method = RequestMethod.GET)
    public String viewExperiment(final Map<String, Object> model, @PathVariable("experimentId") int experimentId) {
        populateAlgorithms(model);

        model.put("title", experimentId);
        try {
            RunnableExperiment experiment = experimentExecutor.getExperiment(experimentId);

            mixPanelService.log(getUser(), "experiment", "view", experiment.getAlgorithmName());

            LOGGER.info(experiment);
            //System.out.println("experiment:" + experiment.getExperiment().getPopulation().getNodes().size() + "nodes");
            model.put("population", experiment.getExperiment().getPopulation());
            model.put("nodes", experiment.getExperiment().getPopulation().getNodes());
            ArrayList<String> edges = new ArrayList<>();
            Iterator<PopulationLink> edgesIterator = experiment.getExperiment().getPopulation().getEdges().iterator();
            while (edgesIterator.hasNext()) {
                PopulationLink currentEdge = edgesIterator.next();
                if (currentEdge.getState().equals("1")) {
                    edges.add(currentEdge.getDefaultEdge().toString());
                }
            }

//        StringBuilder sb = new StringBuilder();
//        try {
//            BufferedReader br = new BufferedReader(new FileReader("exp.out"));
//            String line;
//            while ((line = br.readLine()) != null) {
//                // process the line.
//                sb.append(line).append("\n");
//            }
//            br.close();
//        } catch (Exception e) {
//            LOGGER.error(e, e);
//        }
            model.put("logs", "");
            model.put("edges", edges);
            model.put("experiment", experiment);
            model.put("experimentId", experimentId);

            return "experiment/view";
        } catch (Exception e) {
            return "redirect:/experiment";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public Map<Long, Long> viewExperiment(
            @RequestParam("algorithm") String algorithm
            , @RequestParam("scheduler") String scheduler
            , @RequestParam(value = "size", required = false) Long size
            , @RequestParam("stat") String stat
    ) {
        Map<Long, Long> response = new HashMap<>();
        if (size == null) {
            final Set<Experiment> experiments = experimentRepository.findByAlgorithmAndScheduler(algorithm, scheduler);
            for (final Experiment experiment : experiments) {
                if (experiment.getPopulationSize() != null && !response.containsKey(experiment.getPopulationSize())) {
                    response.put(experiment.getPopulationSize(), getAverateStat(algorithm, scheduler, experiment.getPopulationSize(), stat));
                }
            }
        } else {

            response.put(size, getAverateStat(algorithm, scheduler, size, stat));
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/api/populationSizes/{algorithm}/{scheduler}", method = RequestMethod.GET)
    public Set<Long> viewExperiment(@PathVariable("algorithm") String algorithm, @PathVariable("scheduler") String scheduler) {
        return experimentRepository.findPopulationSizeByAlgorithmAndScheduler(algorithm, scheduler);
    }

    @ResponseBody
    @RequestMapping(value = "/api/populationSizes/{algorithm}", method = RequestMethod.GET)
    public Set<Long> viewExperiment(@PathVariable("algorithm") String algorithm) {
        return experimentRepository.findPopulationSizeByAlgorithm(algorithm);
    }

    @ResponseBody
    @RequestMapping(value = "/api/stats/{algorithm}", method = RequestMethod.GET)
    public Set<Experiment> viewExperimentStats(@PathVariable("algorithm") String algorithm) {
        return experimentRepository.findStatsByAlgorithmAndScheduler(algorithm);
    }

    @ResponseBody
    @RequestMapping(value = "/coef", method = RequestMethod.GET)
    public Map<Long, Long> viewCoef(
            @RequestParam("algorithm") String algorithm
            , @RequestParam("scheduler") String scheduler
            , @RequestParam(value = "size", required = false) Long size
            , @RequestParam("stat") String stat
    ) {
        Map<Long, Long> response = new HashMap<>();
        if (size == null) {
            final Set<Experiment> experiments = experimentRepository.findByAlgorithmAndScheduler(algorithm, scheduler);
            for (final Experiment experiment : experiments) {
                if (experiment.getPopulationSize() != null && !response.containsKey(experiment.getPopulationSize())) {
                    response.put(experiment.getPopulationSize(), getAverateStat(algorithm, scheduler, experiment.getPopulationSize(), stat));
                }
            }
        } else {

            response.put(size, getAverateStat(algorithm, scheduler, size, stat));
        }
        return response;
    }

    private long getAverateStat(String algorithm, String scheduler, long size, String stat) {
        SummaryStatistics summaryStatistics = new SummaryStatistics();
        final Set<TerminationStat> items = terminationStatRepository.findByExperimentAlgorithmAndExperimentSchedulerAndExperimentPopulationSize(algorithm, scheduler, size);
        for (final TerminationStat item : items) {
            if (item.getName().equals(stat)) {
                summaryStatistics.addValue(Double.parseDouble(item.getData()));
            }
        }
        return (long) summaryStatistics.getMean();
    }
}
