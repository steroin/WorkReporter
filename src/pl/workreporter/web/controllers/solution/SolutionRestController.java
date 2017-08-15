package pl.workreporter.web.controllers.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.security.login.CompleteUserDetails;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.project.ProjectDao;
import pl.workreporter.web.beans.entities.solution.Solution;
import pl.workreporter.web.beans.entities.solution.SolutionDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

/**
 * Created by Sergiusz on 13.08.2017.
 */
@RestController
public class SolutionRestController {

    @Autowired
    SolutionDao solutionDao;

    @RequestMapping(value = "/solution/solutions", method = GET)
    public Map<String, Object> getManagedSolutions() {
        long userId = ((CompleteUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Map<Long, String> managedSolutions = solutionDao.getSolutionIdNameMap(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("managedSolutions", managedSolutions);
        for (Map.Entry<Long, String> entry : managedSolutions.entrySet()) {
            result.put("firstSolutionId", entry.getKey());
            break;
        }
        return result;
    }

    @RequestMapping(value = "/solution/solutions/{id}", method = GET)
    public @ResponseBody Solution getSolution(@PathVariable("id") long solutionId) {
        Solution result = solutionDao.loadSolution(solutionId);
        return result;
    }

    @RequestMapping(value="/solution/solutions/{id}", method = PATCH)
    public void updateSolutionName(@PathVariable("id") long solutionId, @RequestParam("name") String newName) {
        solutionDao.updateSolutionName(solutionId, newName);
    }
}
