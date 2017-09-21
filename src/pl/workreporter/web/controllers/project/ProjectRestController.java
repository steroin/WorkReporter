package pl.workreporter.web.controllers.project;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.project.ProjectDao;
import pl.workreporter.web.beans.entities.project.ProjectDaoWrapper;
import pl.workreporter.web.beans.entities.projectassociation.ProjectAssociationDao;
import pl.workreporter.web.beans.entities.projectassociation.ProjectAssociationDaoWrapper;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Sergiusz on 15.08.2017.
 */
@RestController
public class ProjectRestController {
    @Autowired
    private ProjectDaoWrapper projectDaoWrapper;
    @Autowired
    private ProjectAssociationDaoWrapper projectAssociationDaoWrapper;

    @JsonView(JsonDataView.SolutionManager.class)
    @RequestMapping(value = "/solution/projects", method = GET)
    public @ResponseBody
    RestResponse<List<Project>> getAllProjects(@RequestParam("id") long solutionId) {
        return projectDaoWrapper.getAllProjectsInSolution(solutionId);
    }

    @RequestMapping(value = "/solution/projects/{id}", method = DELETE)
    public RestResponse<Void> removeProject(@PathVariable("id") long projectId) {
        return projectDaoWrapper.removeProject(projectId);
    }

    @RequestMapping(value = "/solution/projects", method = DELETE)
    public RestResponse<Void> removeSelectedProjects(@RequestParam("projects") List<Long> projects) {
        return projectDaoWrapper.removeProjects(projects);
    }

    @JsonView(JsonDataView.SolutionManager.class)
    @RequestMapping(value = "/solution/projects/{id}", method = PATCH)
    public RestResponse<Project> updateProject(@PathVariable("id") long projectId, @RequestBody Map<String, String> map) {
        return projectDaoWrapper.updateProject(projectId, map);
    }

    @JsonView(JsonDataView.SolutionManager.class)
    @RequestMapping(value="/solution/projects", method = POST)
    public RestResponse<Project> addProject(@RequestBody Map<String, String> project) {
        return projectDaoWrapper.addProject(Long.parseLong(project.get("solutionid")),
                project.get("name"), project.get("description"));
    }

    @RequestMapping(value = "/solution/projects", params = "teamid", method = GET)
    public @ResponseBody
    RestResponse<List<Map<String, String>>> getAllProjectsInTeam(@RequestParam("teamid") long teamId) {
        return projectAssociationDaoWrapper.getTeamsProjects(teamId);
    }

    @RequestMapping(value = "/solution/projectsteams/{projectid}", method = PATCH)
    public RestResponse<Void> updateAssociatedProjectsState(@PathVariable("projectid") long teamId,
                                              @RequestBody Map<String, List<Long>> teams) {
        return projectAssociationDaoWrapper.updateProjectsTeamsState(teamId, teams.get("teamsToAdd"), teams.get("teamsToRemove"));
    }

    @JsonView(JsonDataView.User.class)
    @RequestMapping(value = "/entries/projects", params="userid", method = GET)
    public @ResponseBody
    RestResponse<List<Project>> getAllUsersProjects(@RequestParam("userid") long userId) {
        return projectDaoWrapper.getAllUsersProject(userId);
    }
}
