package pl.workreporter.web.controllers.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.security.authentication.CompleteUserDetails;
import pl.workreporter.web.beans.entities.solution.Solution;
import pl.workreporter.web.beans.entities.solution.SolutionDao;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

/**
 * Created by Sergiusz on 13.08.2017.
 */
@RestController
public class SolutionRestController {

    @Autowired
    private SolutionDao solutionDao;

    @RequestMapping(value = "/solution/solutions", method = GET)
    public RestResponse<Map<String, Object>> getManagedSolutions() {
        long userId = ((CompleteUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Map<Long, String> managedSolutions = solutionDao.getSolutionIdNameMap(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("managedSolutions", managedSolutions);
        for (Map.Entry<Long, String> entry : managedSolutions.entrySet()) {
            result.put("firstSolutionId", entry.getKey());
            break;
        }
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/solution/solutions/{id}", method = GET)
    public @ResponseBody RestResponse<Solution> getSolution(@PathVariable("id") long solutionId) {
        Solution result = solutionDao.loadSolution(solutionId);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value="/solution/solutions/{id}", method = PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<Void> updateSolutionName(@PathVariable("id") long solutionId, @RequestBody Solution newSolution) {
        solutionDao.updateSolution(newSolution);
        return new RestResponseSuccess<>();
    }
}
