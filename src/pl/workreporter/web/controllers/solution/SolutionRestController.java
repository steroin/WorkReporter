package pl.workreporter.web.controllers.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.security.authentication.CompleteUserDetails;
import pl.workreporter.web.beans.entities.solution.Solution;
import pl.workreporter.web.beans.entities.solution.SolutionDao;
import pl.workreporter.web.beans.entities.solution.SolutionDaoWrapper;
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
    private SolutionDaoWrapper solutionDaoWrapper;

    @RequestMapping(value = "/solution/solutions/{id}", method = GET)
    public @ResponseBody RestResponse<Solution> getSolution(@PathVariable("id") long solutionId) {
        return solutionDaoWrapper.loadSolution(solutionId);
    }

    @RequestMapping(value="/solution/solutions/{id}", method = PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<Solution> updateSolutionName(@PathVariable("id") long solutionId, @RequestBody Solution newSolution) {
        return solutionDaoWrapper.updateSolution(newSolution);
    }
}
