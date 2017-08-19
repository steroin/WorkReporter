package pl.workreporter.web.controllers.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.project.ProjectDao;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Sergiusz on 15.08.2017.
 */
@RestController
public class ProjectRestController {
    @Autowired
    private ProjectDao projectDao;

    @RequestMapping(value = "/solution/projects", method = GET)
    public @ResponseBody
    List<Project> getAllProjects(@RequestParam("id") long solutionId) {
        List<Project> result = projectDao.getAllProjectsInSolution(solutionId);
        return result;
    }

    @RequestMapping(value = "/solution/projects/{id}", method = DELETE)
    public void removeProject(@RequestParam("solutionid") long solutionId, @PathVariable("id") long projectId) {
        projectDao.removeProject(solutionId, projectId);
    }

    @RequestMapping(value = "/solution/projects", method = DELETE)
    public void removeSelectedProjects(@RequestParam("solutionid") long solutionId, @RequestParam("projects") List<Long> projects) {
        projectDao.removeProjects(solutionId, projects);
    }

    @RequestMapping(value = "/solution/projects/{id}", method = PATCH)
    public void updateProject(@PathVariable("id") long projectId, @RequestBody Project project) {
        projectDao.updateProject(project);
    }

    @RequestMapping(value="/solution/projects", method = POST)
    public Project addProject(@RequestBody Map<String, String> project) {
        return projectDao.addProject(Long.parseLong(project.get("solutionid")), project.get("name"), project.get("description"));
    }
}
