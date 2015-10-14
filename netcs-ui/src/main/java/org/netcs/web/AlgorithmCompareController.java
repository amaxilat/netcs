//package org.netcs.web;
//
//import org.apache.log4j.Logger;
//import org.netcs.model.mongo.AlgorithmStatistics;
//import org.netcs.model.mongo.AlgorithmStatisticsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.annotation.PostConstruct;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by amaxilatis on 9/20/14.
// */
//@Controller
//public class AlgorithmCompareController extends BaseController {
//    /**
//     * a log4j logger to print messages.
//     */
//    private static final Logger LOGGER = Logger.getLogger(AlgorithmCompareController.class);
//
//    @Autowired
//    AlgorithmStatisticsRepository algorithmStatisticsRepository;
//
//    @PostConstruct
//    public void init() {
//    }
//
//
//    @RequestMapping(value = "/algorithm/compare", method = RequestMethod.GET)
//    public String addAlgorithm(final Map<String, Object> model, @RequestParam final String[] algorithms, @RequestParam(required = false, defaultValue = "100") final long from) {
//        populateAlgorithms(model);
//        Set<AlgorithmStatistics> algorithmStatisticses = new HashSet<>();
//        for (String algorithm : algorithms) {
//            algorithmStatisticses.add(algorithmStatisticsRepository.findByAlgorithmName(algorithm));
//        }
//        model.put("algorithms", algorithmStatisticses);
//        model.put("from", from);
//        return "algorithm/compare";
//    }
//}
