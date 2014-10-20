package org.netcs.web;

import org.netcs.ExperimentExecutor;
import org.netcs.config.ConfigFile;
import org.netcs.model.mongo.Algorithm;
import org.netcs.model.mongo.AlgorithmRepository;
import org.netcs.model.mongo.AlgorithmStatistics;
import org.netcs.model.mongo.AlgorithmStatisticsRepository;
import org.netcs.model.mongo.ExecutionStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class AlgorithmController {

    @Autowired
    ExperimentExecutor experimentExecutor;
    @Autowired
    AlgorithmRepository algorithmRepository;
    @Autowired
    AlgorithmStatisticsRepository algorithmStatisticsRepository;

    @PostConstruct
    public void init() throws Exception {
    }

    @RequestMapping(value = "/algorithm/{algorithm}", method = RequestMethod.GET)
    public String home(final Map<String, Object> model, @PathVariable("algorithm") final String algorithm) {
        model.put("title", "View " + algorithm);

        try {
            final ConfigFile configFile = new ConfigFile(algorithm + ".prop");
            model.put("configFile", configFile);
            Algorithm algorithmObj = algorithmRepository.findByName(algorithm);
            model.put("algorithm", algorithmObj);
            AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithm(algorithmObj);
            model.put("stats", stats);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "algorithm/view";
    }

    @ResponseBody
    @RequestMapping(value = "/algorithm/{algorithm}/ocoef", method = RequestMethod.GET, produces = "text/plain")
    public String ocoef(@PathVariable("algorithm") final String algorithm) {

        Algorithm algorithmObj = algorithmRepository.findByName(algorithm);

        AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithm(algorithmObj);
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

    @RequestMapping(value = "/algorithm/{algorithm}", method = RequestMethod.POST)
    public String startExperiment(final Map<String, Object> model,
                                  @PathVariable("algorithm") final String algorithm,
                                  @RequestParam("populationSize") final String populationSize,
                                  @RequestParam("populationSizeEnd") final String populationSizeEnd,
                                  @RequestParam("iterations") final String iterations
    ) {
        Algorithm algorithmObj = algorithmRepository.findByName(algorithm);
        if (algorithmObj == null) {
            algorithmObj = new Algorithm();
            algorithmObj.setName(algorithm);
            algorithmRepository.save(algorithmObj);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    experimentExecutor.start(new String[]{algorithm + ".prop", "exp.out", populationSize, iterations, populationSizeEnd});
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
        return "redirect:/experiment";
    }
}
