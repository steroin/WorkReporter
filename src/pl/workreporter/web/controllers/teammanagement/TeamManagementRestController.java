package pl.workreporter.web.controllers.teammanagement;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.logentry.LogEntry;
import pl.workreporter.web.beans.entities.logentry.LogEntryDaoWrapper;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.entities.team.TeamDaoWrapper;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.entities.user.UserDaoWrapper;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

/**
 * Created by Sergiusz on 25.08.2017.
 */
@RestController
public class TeamManagementRestController {

    @Autowired
    private TeamDaoWrapper teamDaoWrapper;
    @Autowired
    private UserDaoWrapper userDaoWrapper;
    @Autowired
    private LogEntryDaoWrapper logEntryDaoWrapper;

    @RequestMapping("/teams")
    @JsonView(JsonDataView.User.class)
    public RestResponse<List<Team>> getManagedTeam(@RequestParam("userid") long userId) {
        return teamDaoWrapper.getAllTeamsManagedBy(userId);
    }

    @RequestMapping(value = "/teams/{id}/employees", method = GET)
    @JsonView(JsonDataView.User.class)
    public @ResponseBody
    RestResponse<List<User>> getAllEmployeesInTeam(@PathVariable("id") long teamId) {
        return userDaoWrapper.getAllUsersInTeam(teamId);
    }

    @RequestMapping(value = "/teams/{teamid}/employees/{userid}", method = GET)
    @JsonView(JsonDataView.User.class)
    public @ResponseBody
    RestResponse<List<LogEntry>> getAllEmployeesLogEntries(@PathVariable("teamid") long teamId, @PathVariable("userid") long userId, @RequestParam("period") int period) {
        return logEntryDaoWrapper.getLastLogEntries(userId, period);
    }

    @RequestMapping(value = "/teams/{teamid}/employees/{userid}/entries/{entryid}", method = PATCH)
    public RestResponse<Void> changeLogEntryStatus(@PathVariable("teamid") long teamId,
                                     @PathVariable("userid") long userId,
                                     @PathVariable("entryid") long entryId,
                                     @RequestBody int status) {
        return logEntryDaoWrapper.changeLogEntryStatus(entryId, status);
    }
}
