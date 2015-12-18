package org.netcs.web;

import org.apache.log4j.Logger;
import org.netcs.component.ExperimentExecutor;
import org.netcs.config.ConfigFile;
import org.netcs.config.Transition;
import org.netcs.model.mongo.Algorithm;
import org.netcs.model.mongo.AlgorithmStatistics;
import org.netcs.model.mongo.AlgorithmStatisticsRepository;
import org.netcs.model.mongo.ExecutionStatistics;
import org.netcs.model.sql.ExperimentRepository;
import org.netcs.model.sql.UserAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class AlgorithmCreateController extends BaseController {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(AlgorithmCreateController.class);

    @Autowired
    ExperimentExecutor experimentExecutor;
    @Autowired
    AlgorithmStatisticsRepository algorithmStatisticsRepository;
    @Autowired
    ExperimentRepository experimentRepository;

    @RequestMapping(value = "/algorithm/create", method = RequestMethod.POST)
    public String createAlgorithm(final Map<String, Object> model,
                                  @RequestParam("name") final String name,
                                  @RequestParam("construction") final String construction,
                                  @RequestParam("transitionsText") final String transitionsText,
                                  @RequestParam("initialNodeState") final String initialNodeState,
                                  @RequestParam("initialLinkState") final String initialLinkState,
                                  @RequestParam("scheduler") final String scheduler,
                                  @RequestParam("version") final String version) {
        String fullName = "u" + getUser().getId() + "-" + construction + "-" + name;
        AlgorithmStatistics existing = algorithmStatisticsRepository.findByAlgorithmName(fullName);
        if (existing == null) {
            final TransitionsResponse transitions = parseTransitions(transitionsText);


            existing = new AlgorithmStatistics();
            existing.setVersion(Integer.parseInt(version));
            existing.setStatistics(new ArrayList<ExecutionStatistics>());
            existing.setAlgorithm(new Algorithm());
            existing.getAlgorithm().setName(fullName);
            existing.getAlgorithm().setTransitions(transitions.getTransitions());
            existing.getAlgorithm().setConfigFile(new ConfigFile());

            existing.getAlgorithm().getConfigFile().setPopulationSize(100);
            existing.getAlgorithm().getConfigFile().setInitialLinkState(initialLinkState);
            existing.getAlgorithm().getConfigFile().setInitialNodeState(initialNodeState);
            existing.getAlgorithm().getConfigFile().setIterations(100);
            existing.getAlgorithm().getConfigFile().setScheduler(scheduler);
            existing.getAlgorithm().getConfigFile().setTransitions(transitions.getTransitions());

            existing = algorithmStatisticsRepository.save(existing);

            UserAlgorithm userAlgorithm = new UserAlgorithm();
            userAlgorithm.setUser(getUser());
            userAlgorithm.setAlgorithmId(existing.getId());
            userAlgorithmRepository.save(userAlgorithm);
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
