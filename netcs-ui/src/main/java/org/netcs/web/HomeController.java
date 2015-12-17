package org.netcs.web;

import org.apache.log4j.Logger;
import org.netcs.component.ExperimentExecutor;
import org.netcs.model.sql.Role;
import org.netcs.model.sql.User;
import org.netcs.model.sql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by amaxilatis on 9/20/14.
 */
@Controller
public class HomeController extends BaseController {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = Logger.getLogger(HomeController.class);

    @Autowired
    ExperimentExecutor experimentExecutor;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void init() {
        //
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(final Map<String, Object> model) {
        populateAlgorithms(model);
        model.put("title", "Home");
        model.put("user", getUser());
        return "home";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getUserCreatePage() {
        return "register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "login";
    }

    /**
     * The controller used to sign users up using only the email address.
     *
     * @param model the map containing the model.
     * @return the corresponding view.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postCreate(final Map<String, Object> model,
                             @RequestParam final String email,
                             @RequestParam final String password,
                             @RequestParam final String password2) {

        LOGGER.info(String.format("Trying to create a new user account with email %s", email));

        //Check if both passwords match
        if (!password.equals(password2)) {
            LOGGER.error("Passwords do not match. Showing error page.");
            model.put("passwordsError", true);
            return "register";
        }

        if ("".equals(password)) {
            LOGGER.error("Password was empty. Showing error page.");
            model.put("passwordEmpty", true);
            return "register";
        }

        //Check if a User exists with the given email
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            LOGGER.error(String.format("User already exists with email %s. Showing error page.", email));

            model.put("userExists", true);
            return "register";
        }

        //Create and save the new User
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setRole(Role.USER);
        newUser.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(newUser);

        LOGGER.info(String.format("Successfully created new User with ID %d", newUser.getId()));

        return "redirect:/login";
    }

}
