package pl.workreporter.web.controllers.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.position.Position;
import pl.workreporter.web.beans.entities.projectassociation.ProjectAssociationDao;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.entities.team.TeamDao;

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
    List<Team> getAllTeams(@RequestParam("id") long solutionId) {
        List<Team> result = teamDao.getAllTeamsInSolution(solutionId);
        return result;
    }

    @RequestMapping(value = "/solution/teams/{id}", method = DELETE)
    public void removeTeam(@RequestParam("solutionid") long solutionId, @PathVariable("id") long teamId) {
        teamDao.removeTeam(solutionId, teamId);
    }

    @RequestMapping(value = "/solution/teams", method = DELETE)
    public void removeSelectedTeams(@RequestParam("solutionid") long solutionId, @RequestParam("teams") List<Long> teams) {
        teamDao.removeTeams(solutionId, teams);
    }

    @RequestMapping(value = "/solution/teams/{id}", method = PATCH)
    public void updateTeam(@PathVariable("id") long teamId, @RequestBody Team team) {
        teamDao.updateTeam(team);
    }

    @RequestMapping(value="/solution/teams", method = POST)
    public Team addTeam(@RequestBody Team team) {
        return teamDao.addTeam(team.getSolutionId(), team.getName(), team.getLeaderId());
    }

    @RequestMapping(value = "/solution/teams", params = "projectid", method = GET)
    public @ResponseBody
    List<Map<String, String>> getAllTeamsWithProject(@RequestParam("projectid") long projectid) {
        List<Map<String, String>> result = projectAssociationDao.getProjectsTeams(projectid);
        return result;
    }

    @RequestMapping(value = "/solution/teamsprojects/{teamid}", method = PATCH)
    public void updateAssociatedProjectsState(@PathVariable("teamid") long teamId,
                                              @RequestBody Map<String, List<Long>> projects) {
        projectAssociationDao.updateTeamsProjectsState(teamId, projects.get("projectsToAdd"), projects.get("projectsToRemove"));
    }
}
