package org.netcs.web;

import org.apache.log4j.Logger;
import org.netcs.ExperimentExecutor;
import org.netcs.config.ConfigFile;
import org.netcs.config.Transition;
import org.netcs.model.mongo.Algorithm;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

    @PostConstruct
    public void init() {
    }


    @RequestMapping(value = "/algorithm/add", method = RequestMethod.GET)
    public String addAlgorithm(final Map<String, Object> model) {
        populateAlgorithms(model);
        model.put("title", "Add algorithm");

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
        model.put("title", "View " + algorithm);

        AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        model.put("stats", stats);

        return "algorithm/view";
    }

    @ResponseBody
    @RequestMapping(value = "/algorithm/{algorithm}/ocoef", method = RequestMethod.GET, produces = "text/plain")
    public String ocoef(@PathVariable("algorithm") final String algorithm) {

        AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
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

        AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
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

        AlgorithmStatistics stats = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
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
                                  @RequestParam("populationSize") final String populationSize,
                                  @RequestParam("populationSizeEnd") final String populationSizeEnd,
                                  @RequestParam("iterations") final String iterations
    ) {
        populateAlgorithms(model);

        final AlgorithmStatistics algorithmObject = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        if (algorithmObject.getVersion() == 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        experimentExecutor.start(algorithmObject.getAlgorithm(), Long.parseLong(populationSize),
                                Long.parseLong(iterations), Long.parseLong(populationSizeEnd));
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        experimentExecutor.start(algorithmObject.getAlgorithm(), Long.parseLong(populationSize),
                                Long.parseLong(iterations), Long.parseLong(populationSizeEnd));
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
        }
        return "redirect:/experiment";
    }

    @RequestMapping(value = "/algorithm2/{algorithm}", method = RequestMethod.POST)
    public String startExperiment2(@PathVariable("algorithm") final String algorithm,
                                   @RequestParam("populationSize") final String populationSize,
                                   @RequestParam("populationSizeEnd") final String populationSizeEnd,
                                   @RequestParam("iterations") final String iterations
    ) {
        final AlgorithmStatistics algorithmObject = algorithmStatisticsRepository.findByAlgorithmName(algorithm);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    experimentExecutor.start2(algorithmObject.getAlgorithm(), Long.parseLong(populationSize),
                            Long.parseLong(iterations), Long.parseLong(populationSizeEnd));
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

    @RequestMapping(value = "/algorithm/create", method = RequestMethod.POST)
    public String createAlgorithm(final Map<String, Object> model,
                                  @RequestParam("name") final String name,
                                  @RequestParam("transitionsText") final String transitionsText,
                                  @RequestParam("initialNodeState") final String initialNodeState,
                                  @RequestParam("initialLinkState") final String initialLinkState,
                                  @RequestParam("scheduler") final String scheduler,
                                  @RequestParam("version") final String version
    ) {

        AlgorithmStatistics existing = algorithmStatisticsRepository.findByAlgorithmName(name);
        if (existing == null) {
            final TransitionsResponse transitions = parseTransitions(transitionsText);


            existing = new AlgorithmStatistics();
            existing.setVersion(Integer.parseInt(version));
            existing.setStatistics(new ArrayList<ExecutionStatistics>());
            existing.setAlgorithm(new Algorithm());
            existing.getAlgorithm().setName(name);
            existing.getAlgorithm().setTransitions(transitions.getTransitions());
            existing.getAlgorithm().setConfigFile(new ConfigFile());

            existing.getAlgorithm().getConfigFile().setPopulationSize(100);
            existing.getAlgorithm().getConfigFile().setInitialLinkState(initialLinkState);
            existing.getAlgorithm().getConfigFile().setInitialNodeState(initialNodeState);
            existing.getAlgorithm().getConfigFile().setIterations(100);
            existing.getAlgorithm().getConfigFile().setScheduler(scheduler);
            existing.getAlgorithm().getConfigFile().setTransitions(transitions.getTransitions());

            algorithmStatisticsRepository.save(existing);
            updateMenu();
        }
        return "redirect:/experiment";
    }


    @ResponseBody
    @RequestMapping(value = "/transitions/parse", method = RequestMethod.POST)
    public TransitionsResponse parse(@RequestParam("transitionsText") final String transitionsText) {
        return parseTransitions(transitionsText);
    }

    private TransitionsResponse parseTransitions(final String transitionsText) {
        final List<Transition> transitions = new ArrayList<>();
        Set<String> nodeStates = new HashSet<>();
        Set<String> linkStates = new HashSet<>();
        final String[] lines = transitionsText.split("\n");
        for (final String line : lines) {
            if (!line.startsWith("#")) {
                final String cline = line.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("->", ",").replaceAll(" ", "").replaceAll("\r", "").replaceAll("\n", "");
                final String[] parts = cline.split(",");
                LOGGER.info(line);
                LOGGER.info(parts);
                LOGGER.info(parts.length);
                if (parts.length >= 6) {
                    Transition newTransition = new Transition(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    nodeStates.add(parts[0]);
                    nodeStates.add(parts[1]);
                    nodeStates.add(parts[3]);
                    nodeStates.add(parts[4]);
                    linkStates.add(parts[2]);
                    linkStates.add(parts[5]);
                    LOGGER.info(newTransition.toString());
                    transitions.add(newTransition);
                }
            }
        }
        LOGGER.info("Read " + transitions.size() + " transitions.");

        return new TransitionsResponse(transitions, nodeStates, linkStates);
    }

    class TransitionsResponse {

        private final List<Transition> transitions;
        private final Set<String> nodeStates;
        private final Set<String> linkStates;

        public TransitionsResponse(final List<Transition> transitions, final Set<String> nodeStates, final Set<String> linkStates) {
            this.transitions = transitions;
            this.nodeStates = nodeStates;
            this.linkStates = linkStates;
        }

        public List<Transition> getTransitions() {
            return transitions;
        }

        public Set<String> getNodeStates() {
            return nodeStates;
        }

        public Set<String> getLinkStates() {
            return linkStates;
        }
    }
}
