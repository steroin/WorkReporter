package pl.workreporter.web.controllers.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.workreporter.security.login.CompleteUserDetails;
import pl.workreporter.web.beans.entities.solution.Solution;
import pl.workreporter.web.beans.entities.solution.SolutionDao;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Sergiusz on 13.08.2017.
 */
@RestController
public class SolutionResourcesController {

    @Autowired
    SolutionDao solutionDao;

    @RequestMapping(value = "/solution/managedSolutions", method = GET)
    public Map<String, Object> managedSolutions() {
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

    @RequestMapping(value = "/solution/solution", method = GET)
    public @ResponseBody Solution managedSolutions(@RequestParam("id") long solutionId) {
        Solution result = solutionDao.loadSolution(solutionId);
        return result;
    }
}
