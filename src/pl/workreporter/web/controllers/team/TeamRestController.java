package pl.workreporter.web.controllers.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.position.Position;
import pl.workreporter.web.beans.entities.projectassociation.ProjectAssociationDao;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.entities.team.TeamDao;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;

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
    private TeamDao teamDao;
    @Autowired
    private ProjectAssociationDao projectAssociationDao;

    @RequestMapping(value = "/solution/teams", params = "id", method = GET)
    public @ResponseBody
    RestResponse<List<Team>> getAllTeams(@RequestParam("id") long solutionId) {
        List<Team> result = teamDao.getAllTeamsInSolution(solutionId);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/solution/teams/{id}", method = DELETE)
    public RestResponse<Void> removeTeam(@RequestParam("solutionid") long solutionId, @PathVariable("id") long teamId) {
        teamDao.removeTeam(solutionId, teamId);
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/solution/teams", method = DELETE)
    public RestResponse<Void> removeSelectedTeams(@RequestParam("solutionid") long solutionId, @RequestParam("teams") List<Long> teams) {
        teamDao.removeTeams(solutionId, teams);
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/solution/teams/{id}", method = PATCH)
    public RestResponse<Team> updateTeam(@PathVariable("id") long teamId, @RequestBody Map<String, String> map) {

        return new RestResponseSuccess<>(teamDao.updateTeam(teamId, map));
    }

    @RequestMapping(value="/solution/teams", method = POST)
    public RestResponse<Team> addTeam(@RequestBody Map<String, String> map) {
        return new RestResponseSuccess<>(teamDao.addTeam(Long.parseLong(map.get("solutionId")), map.get("name"), null));
    }

    @RequestMapping(value = "/solution/teams", params = "projectid", method = GET)
    public @ResponseBody
    RestResponse<List<Map<String, String>>> getAllTeamsWithProject(@RequestParam("projectid") long projectid) {
        List<Map<String, String>> result = projectAssociationDao.getProjectsTeams(projectid);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/solution/teamsprojects/{teamid}", method = PATCH)
    public RestResponse<Void> updateAssociatedProjectsState(@PathVariable("teamid") long teamId,
                                              @RequestBody Map<String, List<Long>> projects) {
        projectAssociationDao.updateTeamsProjectsState(teamId, projects.get("projectsToAdd"), projects.get("projectsToRemove"));
        return new RestResponseSuccess<>();
    }
}
