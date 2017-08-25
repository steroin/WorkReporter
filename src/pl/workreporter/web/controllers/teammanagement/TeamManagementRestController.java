package pl.workreporter.web.controllers.teammanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.logentry.LogEntry;
import pl.workreporter.web.beans.entities.logentry.LogEntryDao;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.entities.team.TeamDao;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.entities.user.UserDao;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Sergiusz on 25.08.2017.
 */
@RestController
public class TeamManagementRestController {

    @Autowired
    private TeamDao teamDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LogEntryDao logEntryDao;

    @RequestMapping("/teams")
    public List<Team> getManagedTeam(@RequestParam("userid") long userId) {
        return teamDao.getAllTeamsManagedBy(userId);
    }

    @RequestMapping(value = "/teams/{id}/employees", method = GET)
    public @ResponseBody
    List<User> getAllEmployeesInTeam(@PathVariable("id") long teamId) {
        List<User> result = userDao.getAllUsersInTeam(teamId);
        return result;
    }

    @RequestMapping(value = "/teams/{teamid}/employees/{userid}", method = GET)
    public @ResponseBody
    List<LogEntry> getAllEmployeesLogEntries(@PathVariable("teamid") long teamId, @PathVariable("userid") long userId, @RequestParam("period") int period) {
        List<LogEntry> result = logEntryDao.getLastLogEntries(userId, period);
        return result;
    }
}
