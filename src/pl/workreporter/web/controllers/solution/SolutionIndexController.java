package pl.workreporter.web.controllers.solution;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sergiusz on 11.08.2017.
 */
@Controller
public class SolutionIndexController {
    @RequestMapping("/solution")
    public String solutionIndex() {
        return "solution/index";
    }
}
