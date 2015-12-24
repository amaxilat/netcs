package org.netcs.web;

import org.apache.log4j.Logger;
import org.netcs.component.ExperimentExecutor;
import org.netcs.config.ConfigFile;
import org.netcs.config.Transition;
import org.netcs.model.mongo.Algorithm;
import org.netcs.model.mongo.AlgorithmStatistics;
import org.netcs.model.mongo.AlgorithmStatisticsRepository;
import org.netcs.model.mongo.ExecutionStatistics;
import org.netcs.model.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class AlgorithmController extends BaseController {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(AlgorithmController.class);

    @Autowired
    ExperimentExecutor experimentExecutor;
    @Autowired
    AlgorithmStatisticsRepository algorithmStatisticsRepository;
    @Autowired
    ExperimentRepository experimentRepository;
    @Autowired
    TerminationConditionRepository terminationConditionRepository;
    @Autowired
    TerminationStatRepository terminationStatRepository;

    @PostConstruct
    public void init() {
//        final AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName("global-star");
//        final List<ExecutionStatistics> tStats = new ArrayList<>();
//        tStats.addAll(stats.getStatistics());
//        for (ExecutionStatistics stat : tStats) {
//
//            if (stat.getScheduler() == null || stat.getScheduler().equals("Series 2") || stat.getScheduler().equals("org.netcs.scheduler.History") ) {
//                stats.getStatistics().remove(stat);
//            }
//        }
//        algorithmStatisticsRepository.save(stats);
    }


    @RequestMapping(value = "/algorithm/add", method = RequestMethod.GET)
    public String addAlgorithm(final Map<String, Object> model) {
        populateAlgorithms(model);
        model.put("title", "Add algorithm");

        mixPanelService.log(getUser(), "algorithm", "view", "add");

        return "algorithm/add";
    }

    @RequestMapping(value = "/algorithm/add-random", method = RequestMethod.GET)
    public String addRandomAlgorithm(final Map<String, Object> model) {
        populateAlgorithms(model);
        model.put("title", "Add Random algorithm");

        return "algorithm/add-random";
    }

    @RequestMapping(value = "/algorithm/{algorithm}", method = RequestMethod.GET)
    public String viewAlgorithm(final Map<String, Object> model, @PathVariable("algorithm") final String algorithm) {
        populateAlgorithms(model);

        if (!canView(getUser(), algorithm)) {
            return "redirect:/";
        }

        mixPanelService.log(getUser(), "algorithm", "view", algorithm);

        final AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        model.put("title", "View " + algorithm);
        model.put("sizes", experimentRepository.findPopulationSizeByAlgorithm(algorithm));
        model.put("numbers", experimentRepository.findStatsByAlgorithmAndScheduler(algorithm));
        model.put("stats", stats);

        return "algorithm/view";
    }

    @RequestMapping(value = "/algorithm/{algorithm}/delete", method = RequestMethod.POST)
    public String viewAlgorithm(final Map<String, Object> model, @PathVariable("algorithm") final String algorithm,
                                @RequestParam("algorithm") final String algorithmText) {
        populateAlgorithms(model);

        if (!canView(getUser(), algorithm)) {
            return "redirect:/";
        }

        mixPanelService.log(getUser(), "algorithm", "delete", algorithm);

        final AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        algorithmStatisticsRepository.delete(stats);

        for (final UserAlgorithm userAlgorithm : userRepository.findByEmail(getUser().getEmail()).getUserAlgorithms()) {
            if (stats.getId().equals(userAlgorithm.getAlgorithmId())) {
                userAlgorithmRepository.delete(userAlgorithm);
                //TODO: check validity
                final Set<Experiment> experiments = experimentRepository.findStatsByAlgorithmAndScheduler(algorithm);
                for (final Experiment experiment : experiments) {
                    terminationConditionRepository.delete(terminationConditionRepository.findByExperiment(experiment));
                }
                experimentRepository.delete(experiments);
                final Set<TerminationStat> terminationStats = terminationStatRepository.findByExperimentAlgorithm(algorithm);
                terminationStatRepository.delete(terminationStats);
            }
        }

        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping(value = "/algorithm/{algorithm}/csv", method = RequestMethod.GET, produces = "text/csv")
    public String viewAlgorithmCsv(final Map<String, Object> model, @PathVariable("algorithm") final String algorithm) {
        if (!canView(getUser(), algorithm)) {
            return "redirect:/";
        }

        mixPanelService.log(getUser(), "algorithm", "download-csv", algorithm);

        final StringBuilder sb = new StringBuilder();

        sb.append("#").append("\t");
        sb.append("scheduler").append("\t");
        sb.append("size").append("\t");
        sb.append("interactions").append("\t");
        sb.append("eff.interactions").append("\t");
        sb.append("\n");

        final AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);

        int count = 0;
        for (final ExecutionStatistics executionStatistics : stats.getStatistics()) {
            sb.append(count++).append("\t");
            sb.append(executionStatistics.getScheduler()).append("\t");
            sb.append(executionStatistics.getPopulationSize()).append("\t");
            sb.append(executionStatistics.getInteractions()).append("\t");
            sb.append(executionStatistics.getEffectiveInteractions()).append("\t");
            sb.append("\n");
        }

        return sb.toString();
    }


    @RequestMapping(value = "/algorithm/{algorithm}/details", method = RequestMethod.GET)
    public String viewAlgorithmDetails(final Map<String, Object> model, @PathVariable("algorithm") final String algorithm) {
        populateAlgorithms(model);

        if (!canView(getUser(), algorithm)) {
            return "redirect:/";
        }

        model.put("title", "View " + algorithm);
        final AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        model.put("stats", stats);

        return "algorithm/details";
    }

    @ResponseBody
    @RequestMapping(value = "/algorithm/{algorithm}/ocoef", method = RequestMethod.GET, produces = "text/plain")
    public String ocoef(@PathVariable("algorithm") final String algorithm) {

        if (!canView(getUser(), algorithm)) {
            return "redirect:/";
        }

        final AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        StringBuilder response = new StringBuilder();
        Map<Long, Double> sizes = new HashMap<>();
        Map<Long, Double> counts = new HashMap<>();
        for (ExecutionStatistics executionStatistics : stats.getStatistics()) {
            if (!sizes.containsKey(sizes)) {
                counts.put(executionStatistics.getPopulationSize(), 1.0);
                sizes.put(executionStatistics.getPopulationSize(), Double.valueOf(executionStatistics.getInteractions()));
            } else {
                counts.put(executionStatistics.getPopulationSize(), counts.get(executionStatistics.getPopulationSize() + 1));
                sizes.put(executionStatistics.getPopulationSize(),
                        sizes.get(executionStatistics.getPopulationSize()) + executionStatistics.getInteractions());
            }
        }
        for (Long aLong : sizes.keySet()) {
            final Double total = sizes.get(aLong);
            sizes.put(aLong, total / counts.get(aLong));
        }
        for (Long aLong : new TreeSet<>(sizes.keySet())) {

            final double bigOmega = Math.pow(aLong, 2);
            response.append(String.format("%d & %f", aLong, sizes.get(aLong) / bigOmega))
                    .append(" \\\\ \n");
        }

        return response.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/algorithm/{algorithm}/counterStats/{b}", method = RequestMethod.GET, produces = "text/plain")
    public String counterStats(@PathVariable("algorithm") final String algorithm, @PathVariable("b") final Double b_target) {

        if (!canView(getUser(), algorithm)) {
            return "redirect:/";
        }

        final AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        StringBuilder response = new StringBuilder();
        Map<Long, Double> interactions = new HashMap<>();
        Map<Long, Double> sizeCounted = new HashMap<>();
        Map<Long, Double> counts = new HashMap<>();
        for (ExecutionStatistics executionStatistics : stats.getStatistics()) {
            if (!executionStatistics.isSuccess()) continue;
            final Double count1 = Double.valueOf(executionStatistics.getTerminationMessage().split(",")[1].split("=")[1]);
            final Double b = Double.valueOf(executionStatistics.getTerminationMessage().split(",")[0].split("=")[1]);
            if (!b.equals(b_target)) continue;
            if (!interactions.containsKey(executionStatistics.getPopulationSize())) {
                counts.put(executionStatistics.getPopulationSize(), 1.0);
                interactions.put(executionStatistics.getPopulationSize(), Double.valueOf(executionStatistics.getInteractions()));
                sizeCounted.put(executionStatistics.getPopulationSize(), count1);
            } else {
                counts.put(executionStatistics.getPopulationSize(), counts.get(executionStatistics.getPopulationSize() + 1));
                interactions.put(executionStatistics.getPopulationSize(),
                        interactions.get(executionStatistics.getPopulationSize()) + executionStatistics.getInteractions());
                sizeCounted.put(executionStatistics.getPopulationSize(),
                        sizeCounted.get(executionStatistics.getPopulationSize()) + count1);
            }
        }
        for (Long aLong : interactions.keySet()) {
            final Double total = interactions.get(aLong);
            interactions.put(aLong, total / counts.get(aLong));
        }
        for (Long aLong : new TreeSet<>(interactions.keySet())) {

            response.append(String.format("%d;%f;%f;%.0f\n", aLong, interactions.get(aLong), sizeCounted.get(aLong), sizeCounted.get(aLong) / aLong * 100));
        }

        return response.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/algorithm/{algorithm}/failureRates/{b}", method = RequestMethod.GET, produces = "text/plain")
    public String failureRates(@PathVariable("algorithm") final String algorithm, @PathVariable("b") final Double b_target) {

        if (!canView(getUser(), algorithm)) {
            return "redirect:/";
        }

        final AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        StringBuilder response = new StringBuilder();
        Map<Long, Double> successfulExperiments = new HashMap<>();
        Map<Long, Double> totalExperiments = new HashMap<>();
        for (ExecutionStatistics executionStatistics : stats.getStatistics()) {
            final Double b = Double.valueOf(executionStatistics.getTerminationMessage().split(",")[0].split("=")[1]);
            if (!b.equals(b_target)) continue;
            Long thisSize = executionStatistics.getPopulationSize();
            if (!successfulExperiments.containsKey(thisSize)) {
                totalExperiments.put(thisSize, 1.0);
                if (executionStatistics.isSuccess()) {
                    successfulExperiments.put(thisSize, 1.0);
                } else {
                    successfulExperiments.put(thisSize, 0.0);
                }
            } else {
                totalExperiments.put(thisSize, totalExperiments.get(thisSize) + 1);
                if (executionStatistics.isSuccess()) {
                    successfulExperiments.put(thisSize,
                            successfulExperiments.get(thisSize) + 1);
                }
            }
        }
        for (Long aLong : totalExperiments.keySet()) {
            final Double totalSuccessfulExperiments = successfulExperiments.get(aLong);
            successfulExperiments.put(aLong, totalSuccessfulExperiments / totalExperiments.get(aLong));
        }
        response.append("size;experiments;failureRate\n");
        for (Long aLong : new TreeSet<>(successfulExperiments.keySet())) {

            response.append(String.format("%d;%.0f;%.2f\n", aLong, totalExperiments.get(aLong), successfulExperiments.get(aLong) * 100));
        }

        return response.toString();
    }

    @RequestMapping(value = "/algorithm/{algorithm}", method = RequestMethod.POST)
    public String startExperiment(final Map<String, Object> model,
                                  @PathVariable("algorithm") final String algorithm,
                                  @RequestParam("populationSize") final String populationSize) {

        if (!canView(getUser(), algorithm)) {
            return "redirect:/";
        }

        mixPanelService.log(getUser(), "algorithm", "run", algorithm);

        populateAlgorithms(model);

        final AlgorithmStatistics algorithmObject = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        if (algorithmObject.getVersion() == 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
//        TODO:
                    try {
                        experimentExecutor.runExperiment(algorithmObject.getAlgorithm(), "Random", Long.parseLong(populationSize));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (algorithmObject.getVersion() == 3) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//        TODO:
//                    try {
//                        experimentExecutor.start(algorithmObject.getAlgorithm(), Long.parseLong(populationSize),
//                                Long.parseLong(iterations), Long.parseLong(populationSizeEnd));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/experiment";
    }

    @RequestMapping(value = "/algorithm2/{algorithm}", method = RequestMethod.POST)
    public String startExperiment2(@PathVariable("algorithm") final String algorithm,
                                   @RequestParam("populationSize") final String populationSize,
                                   @RequestParam("populationSizeEnd") final String populationSizeEnd,
                                   @RequestParam("iterations") final String iterations) {

        if (!canView(getUser(), algorithm)) {
            return "redirect:/";
        }

        final AlgorithmStatistics algorithmObject = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        new Thread(new Runnable() {
            @Override
            public void run() {
//        TODO:
//                try {
//                    experimentExecutor.start2(algorithmObject.getAlgorithm(), Long.parseLong(populationSize),
//                            Long.parseLong(iterations), Long.parseLong(populationSizeEnd));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }).start();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "redirect:/experiment";
    }
}
