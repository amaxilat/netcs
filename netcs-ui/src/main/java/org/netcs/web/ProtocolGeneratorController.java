package org.netcs.web;

import org.apache.log4j.Logger;
import org.netcs.component.ExperimentExecutor;
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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class ProtocolGeneratorController extends BaseController {

    /**
     * Apache Log4J logger.
     */
    protected static final Logger LOGGER = Logger.getLogger(ProtocolGeneratorController.class);

    @Autowired
    ExperimentExecutor experimentExecutor;

    @Autowired
    AlgorithmStatisticsRepository algorithmStatisticsRepository;

    @PostConstruct
    public void init() {

    }

    @RequestMapping(value = "/generate/{size}", method = RequestMethod.GET)
    public String generateRandomGet(@PathVariable("size") final int size) {
        final int randomId = doGenerateRandom(size);
        updateMenu();
        return "redirect:/algorithm/random" + randomId;
    }

    @RequestMapping(value = "/generate/random", method = RequestMethod.POST)
    public String generateRandomPost(@RequestParam("size") final int size) {
        final int randomId = doGenerateRandom(size);
        updateMenu();
        return "redirect:/algorithm/random" + randomId;
    }

    public int doGenerateRandom(final int size) {
        int theCount = 0;
        String name = "random" + theCount;
        while (algorithmStatisticsRepository.findByAlgorithmName(name) != null) {
            theCount++;
            name = "random" + theCount;
        }

        final Algorithm algorithm = generateProtocol(size);
        algorithm.setName(name);
        final AlgorithmStatistics stats = new AlgorithmStatistics();
        stats.setAlgorithm(algorithm);
        stats.setStatistics(new ArrayList<ExecutionStatistics>());
        stats.setVersion(3);
        algorithmStatisticsRepository.save(stats);

        updateMenu();
        return theCount;
    }


    private Algorithm generateProtocol(final int stateCount) {
        final Random random = new Random();
        final Algorithm algorithm = new Algorithm();
        final ConfigFile conf = new ConfigFile();
        algorithm.setConfigFile(conf);

        algorithm.setTransitions(new ArrayList<Transition>());
        for (int i = 0; i < stateCount; i++) {
            for (int j = i; j < stateCount; j++) {
                final Transition t = new Transition("q" + i, "q" + j, "l", "q" + random.nextInt(stateCount), "q" + random.nextInt(stateCount), "l");
                algorithm.getTransitions().add(t);
                LOGGER.info(t);
            }
        }
        conf.setTransitions(algorithm.getTransitions());
        conf.setInitialLinkState("l");
        conf.setInitialNodeState("q0");
        conf.setIterations(100);
        conf.setPopulationSize(100);
        conf.setScheduler("Random");
        return algorithm;
    }
}
