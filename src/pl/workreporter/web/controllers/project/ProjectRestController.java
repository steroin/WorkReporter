package pl.workreporter.web.controllers.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.project.ProjectDao;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

/**
 * Created by Sergiusz on 15.08.2017.
 */
@RestController
public class ProjectRestController {
    @Autowired
    ProjectDao projectDao;

    @RequestMapping(value = "/solution/projects", method = GET)
    public @ResponseBody
    List<Project> getAllProjects(@RequestParam("id") long solutionId) {
        List<Project> result = projectDao.getAllProjectsInSolution(solutionId);
        return result;
    }

    @RequestMapping(value = "/solution/projects/{id}", method = DELETE)
    public void removeProject(@RequestParam("solutionid") long solutionId, @PathVariable("id") long projectId) {
        System.out.println("deleting: "+projectId+" in solution: "+solutionId);
    }

    @RequestMapping(value = "/solution/projects/{id}", method = PATCH)
    public void updateProject(@PathVariable("id") long projectId, @RequestBody Project project) {
        projectDao.updateProject(project);
    }
}
