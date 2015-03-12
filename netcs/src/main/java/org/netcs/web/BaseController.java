package org.netcs.web;

import org.apache.log4j.Logger;
import org.netcs.ExperimentExecutor;
import org.netcs.model.mongo.AlgorithmStatistics;
import org.netcs.model.mongo.AlgorithmStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class BaseController {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(BaseController.class);

    @Autowired
    ExperimentExecutor experimentExecutor;
    @Autowired
    AlgorithmStatisticsRepository algorithmStatisticsRepository;
    private static TreeSet<String> names;

    @PostConstruct
    public void init() {


    }

    protected void populateAlgorithms(final Map<String, Object> model) {
        if (names == null) {
            names = new TreeSet<>(new Comparator<String>() {
                @Override
                public int compare(final String o1, final String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
        }
        if (names.isEmpty()) {
            final List<AlgorithmStatistics> stats = algorithmStatisticsRepository.findAll();
            for (final AlgorithmStatistics stat : stats) {
                names.add(stat.getAlgorithm().getName());
            }
        }
        model.put("algorithmNames", names);
    }

    protected void updateMenu() {
        names = null;
    }
}
