package org.netcs.web;

import org.netcs.component.ExperimentExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class HomeController extends BaseController {

    @Autowired
    ExperimentExecutor experimentExecutor;

    @PostConstruct
    public void init() {
        //
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(final Map<String, Object> model) {
        populateAlgorithms(model);
        model.put("title", "Home");
        return "home";
    }
}
