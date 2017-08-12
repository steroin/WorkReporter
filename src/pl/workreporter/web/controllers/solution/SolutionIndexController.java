package pl.workreporter.web.controllers.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.workreporter.security.login.CompleteUserDetails;
import pl.workreporter.web.beans.entities.solution.SolutionDao;

import java.util.List;

/**
 * Created by Sergiusz on 11.08.2017.
 */
@Controller
public class SolutionIndexController {
    @Autowired
    SolutionDao solutionDao;

    @RequestMapping("/solution")
    public ModelAndView solutionIndex() {
        List<Long> managedSolutions = ((CompleteUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getManagedSolutions();
        if (managedSolutions == null || managedSolutions.size() < 1) {
            return new ModelAndView("solution/error");
        }
        return new ModelAndView("solution/index", "managedSolutions", managedSolutions);
    }
}
