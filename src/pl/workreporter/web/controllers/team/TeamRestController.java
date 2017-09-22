package pl.workreporter.web.controllers.team;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.position.Position;
import pl.workreporter.web.beans.entities.projectassociation.ProjectAssociationDao;
import pl.workreporter.web.beans.entities.projectassociation.ProjectAssociationDaoWrapper;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.entities.team.TeamDao;
import pl.workreporter.web.beans.entities.team.TeamDaoWrapper;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Sergiusz on 20.08.2017.
 */
@RestController
public class TeamRestController {
    @Autowired
    private TeamDaoWrapper teamDaoWrapper;
    @Autowired
    private ProjectAssociationDaoWrapper projectAssociationDaoWrapper;

    @RequestMapping(value = "/solution/teams", params = "id", method = GET)
    @JsonView(JsonDataView.SolutionManager.class)
    public @ResponseBody
    RestResponse<List<Team>> getAllTeams(@RequestParam("id") long solutionId) {
        return teamDaoWrapper.getAllTeamsInSolution(solutionId);
    }

    @RequestMapping(value = "/solution/teams/{id}", method = DELETE)
    @JsonView(JsonDataView.SolutionManager.class)
    public RestResponse<Void> removeTeam(@PathVariable("id") long teamId) {
        return teamDaoWrapper.removeTeam(teamId);
    }

    @RequestMapping(value = "/solution/teams", method = DELETE)
    @JsonView(JsonDataView.SolutionManager.class)
    public RestResponse<Void> removeSelectedTeams(@RequestParam("teams") List<Long> teams) {
        return teamDaoWrapper.removeTeams(teams);
    }

    @RequestMapping(value = "/solution/teams/{id}", method = PATCH)
    @JsonView(JsonDataView.SolutionManager.class)
    public RestResponse<Team> updateTeam(@PathVariable("id") long teamId, @RequestBody Map<String, String> map) {
        return teamDaoWrapper.updateTeam(teamId, map);
    }

    @RequestMapping(value="/solution/teams", method = POST)
    @JsonView(JsonDataView.SolutionManager.class)
    public RestResponse<Team> addTeam(@RequestBody Map<String, String> map) {
        return teamDaoWrapper.addTeam(Long.parseLong(map.get("solutionId")), map.get("name"), null);
    }

    @RequestMapping(value = "/solution/teams", params = "projectid", method = GET)
    @JsonView(JsonDataView.User.class)
    public @ResponseBody
    RestResponse<List<Map<String, String>>> getAllTeamsWithProject(@RequestParam("projectid") long projectid) {
        return projectAssociationDaoWrapper.getProjectsTeams(projectid);
    }

    @RequestMapping(value = "/solution/teamsprojects/{teamid}", method = PATCH)
    @JsonView(JsonDataView.User.class)
    public RestResponse<Void> updateAssociatedProjectsState(@PathVariable("teamid") long teamId,
                                              @RequestBody Map<String, List<Long>> projects) {
        return projectAssociationDaoWrapper.updateTeamsProjectsState(teamId, projects.get("projectsToAdd"), projects.get("projectsToRemove"));
    }
}
