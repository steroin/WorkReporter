package pl.workreporter.web.controllers.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.project.ProjectDao;
import pl.workreporter.web.beans.entities.projectassociation.ProjectAssociationDao;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;

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
    @Autowired
    private ProjectAssociationDao projectAssociationDao;

    @RequestMapping(value = "/solution/projects", method = GET)
    public @ResponseBody
    RestResponse<List<Project>> getAllProjects(@RequestParam("id") long solutionId) {
        List<Project> result = projectDao.getAllProjectsInSolution(solutionId);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/solution/projects/{id}", params="solutionid", method = DELETE)
    public RestResponse<Void> removeProject(@RequestParam("solutionid") long solutionId, @PathVariable("id") long projectId) {
        projectDao.removeProject(solutionId, projectId);
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/solution/projects", method = DELETE)
    public RestResponse<Void> removeSelectedProjects(@RequestParam("solutionid") long solutionId, @RequestParam("projects") List<Long> projects) {
        projectDao.removeProjects(solutionId, projects);
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/solution/projects/{id}", method = PATCH)
    public RestResponse<Project> updateProject(@PathVariable("id") long projectId, @RequestBody Map<String, String> map) {

        return new RestResponseSuccess<>(projectDao.updateProject(projectId, map));
    }

    @RequestMapping(value="/solution/projects", method = POST)
    public RestResponse<Project> addProject(@RequestBody Map<String, String> project) {

        return new RestResponseSuccess<>(projectDao.addProject(Long.parseLong(project.get("solutionid")),
                project.get("name"), project.get("description")));
    }

    @RequestMapping(value = "/solution/projects", params = "teamid", method = GET)
    public @ResponseBody
    RestResponse<List<Map<String, String>>> getAllProjectsInTeam(@RequestParam("teamid") long teamId) {
        List<Map<String, String>> result = projectAssociationDao.getTeamsProjects(teamId);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/solution/projectsteams/{projectid}", method = PATCH)
    public RestResponse<Void> updateAssociatedProjectsState(@PathVariable("projectid") long teamId,
                                              @RequestBody Map<String, List<Long>> teams) {
        projectAssociationDao.updateProjectsTeamsState(teamId, teams.get("teamsToAdd"), teams.get("teamsToRemove"));
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/entries/projects", params="userid", method = GET)
    public @ResponseBody
    RestResponse<List<Project>> getAllUsersProjects(@RequestParam("userid") long userId) {
        return new RestResponseSuccess<>(projectDao.getAllUsersProject(userId));
    }
}
