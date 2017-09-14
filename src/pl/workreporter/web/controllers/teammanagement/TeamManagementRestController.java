package pl.workreporter.web.controllers.teammanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.logentry.LogEntry;
import pl.workreporter.web.beans.entities.logentry.LogEntryDao;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.entities.team.TeamDao;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.entities.user.UserDao;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

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
    public RestResponse<List<Team>> getManagedTeam(@RequestParam("userid") long userId) {
        return new RestResponseSuccess<>(teamDao.getAllTeamsManagedBy(userId));
    }

    @RequestMapping(value = "/teams/{id}/employees", method = GET)
    public @ResponseBody
    RestResponse<List<User>> getAllEmployeesInTeam(@PathVariable("id") long teamId) {
        List<User> result = userDao.getAllUsersInTeam(teamId);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/teams/{teamid}/employees/{userid}", method = GET)
    public @ResponseBody
    RestResponse<List<LogEntry>> getAllEmployeesLogEntries(@PathVariable("teamid") long teamId, @PathVariable("userid") long userId, @RequestParam("period") int period) {
        List<LogEntry> result = logEntryDao.getLastLogEntries(userId, period);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/teams/{teamid}/employees/{userid}/entries/{entryid}", method = PATCH)
    public RestResponse<Void> changeLogEntryStatus(@PathVariable("teamid") long teamId,
                                     @PathVariable("userid") long userId,
                                     @PathVariable("entryid") long entryId,
                                     @RequestBody int status) {
        logEntryDao.changeLogEntryStatus(entryId, status);
        return new RestResponseSuccess<>();
    }
}
