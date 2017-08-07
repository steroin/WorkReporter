package pl.workreporter.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Sergiusz on 07.08.2017.
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("redirect:/login");
    }
}
