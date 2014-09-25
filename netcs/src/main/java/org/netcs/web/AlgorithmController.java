package org.netcs.web;

import org.netcs.ExperimentExecutor;
import org.netcs.config.ConfigFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class AlgorithmController {

    @Autowired
    ExperimentExecutor experimentExecutor;

    @PostConstruct
    public void init() throws Exception {
    }

    @RequestMapping(value = "/algorithm/{algorithm}", method = RequestMethod.GET)
    public String home(final Map<String, Object> model, @PathVariable("algorithm") final String algorithm) {
        model.put("title", "View " + algorithm);

        try {
            final ConfigFile configFile = new ConfigFile("algorithms/" + algorithm + ".prop");
            model.put("configFile", configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "algorithm/view";
    }

    @RequestMapping(value = "/algorithm/{algorithm}", method = RequestMethod.POST)
    public String startExperiment(final Map<String, Object> model,
                                  @PathVariable("algorithm") final String algorithm,
                                  @RequestParam("populationSize") final String populationSize
    ) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    experimentExecutor.start(new String[]{algorithm + ".prop", "exp.out", populationSize, "1"});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "redirect:/experiment/0";
    }
}
