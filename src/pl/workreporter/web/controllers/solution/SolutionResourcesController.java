package pl.workreporter.web.controllers.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.workreporter.security.login.CompleteUserDetails;
import pl.workreporter.web.beans.entities.solution.SolutionDao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergiusz on 13.08.2017.
 */
@RestController
public class SolutionResourcesController {

    @Autowired
    SolutionDao solutionDao;

    @RequestMapping("/solution/managedSolutions")
    public Map<String, Object> managedSolutions() {
        long userId = ((CompleteUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Map<Long, String> managedSolutions = solutionDao.getSolutionIdNameMap(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("managedSolutions", managedSolutions);
        for (Map.Entry<Long, String> entry : managedSolutions.entrySet()) {
            result.put("currentSolution", entry.getKey());
            break;
        }
        return result;
    }
}
