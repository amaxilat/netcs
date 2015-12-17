package org.netcs.web;

import org.apache.log4j.Logger;
import org.netcs.component.ExperimentExecutor;
import org.netcs.model.mongo.AlgorithmStatistics;
import org.netcs.model.mongo.AlgorithmStatisticsRepository;
import org.netcs.model.sql.User;
import org.netcs.model.sql.UserAlgorithm;
import org.netcs.model.sql.UserAlgorithmRepository;
import org.netcs.model.sql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAlgorithmRepository userAlgorithmRepository;

    @PostConstruct
    public void init() {

    }

    /**
     * Retrieve current user from security context holder.
     *
     * @return the User object.
     */
    protected final User getUser() {
        final Object userObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userObject instanceof User) {
            final User theUser = userRepository.findById(((User) userObject).getId());
            return theUser;
        } else {
            return null;
        }
    }

    protected void populateAlgorithms(final Map<String, Object> model) {
        TreeSet<String> names = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });

        if (names.isEmpty()) {
            if (getUser() != null) {
                for (final UserAlgorithm algorithm : getUser().getUserAlgorithms()) {
                    AlgorithmStatistics algorithmMongo = algorithmStatisticsRepository.findById(algorithm.getAlgorithmId());
                    names.add(algorithmMongo.getAlgorithm().getName());
                }
            }
        }
        model.put("algorithmNames", names);
    }

}
