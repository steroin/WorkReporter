package pl.workreporter.web.controllers.logentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.logentry.LogEntryDaoWrapper;
import pl.workreporter.web.service.security.PrincipalAuthenticator;
import pl.workreporter.web.beans.entities.logentry.LogEntry;
import pl.workreporter.web.beans.entities.logentry.LogEntryDao;
import pl.workreporter.web.beans.entities.logentry.LogType;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseAuthenticationError;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Sergiusz on 21.08.2017.
 */
@RestController
public class LogEntryRestController {
    @Autowired
    private LogEntryDaoWrapper logEntryDaoWrapper;
    @Autowired
    private PrincipalAuthenticator authenticator;

    @RequestMapping(value = "/entries", method = GET)
    public @ResponseBody
    RestResponse<List<LogEntry>> getAllLogEntries(@RequestParam("userid") long userId,
                                                 @RequestParam("year") int year,
                                                 @RequestParam("month") int month,
                                                 @RequestParam("day") int day) {
        return logEntryDaoWrapper.getDailyLogEntries(userId, year, month, day);
    }

    @RequestMapping(value = "/entrytypes", method = GET)
    public @ResponseBody
    RestResponse<List<LogType>> getLogEntryTypes() {
        return logEntryDaoWrapper.getAllLogTypes();
    }

    @RequestMapping(value = "/entries", method = POST)
    public RestResponse<LogEntry> addLogEntry(@RequestBody Map<String, String> logEntry) {
        long userId = Long.parseLong(logEntry.get("userid"));
        if (!authenticator.authenticateUserId(userId)) return new RestResponseAuthenticationError<>();

        return logEntryDaoWrapper.addLogEntry(userId,
                logEntry.get("start"),
                Double.parseDouble(logEntry.get("loggedhours")),
                Long.parseLong(logEntry.get("logtypeid")),
                logEntry.get("projectid") == null || logEntry.get("projectid").isEmpty() ? null : Long.parseLong(logEntry.get("projectid")));
    }

    @RequestMapping(value = "/entries/{id}", method = PATCH)
    public RestResponse<Void> updateLogEntry(@PathVariable("id") long entryId, @RequestBody LogEntry logEntry) {
        if (!authenticator.authenticateUserId(logEntry.getUser().getId())) return new RestResponseAuthenticationError<>();
        return logEntryDaoWrapper.updateLogEntry(logEntry);
    }

    @RequestMapping(value = "/entries/{id}", method = DELETE)
    public RestResponse<Void> removeLogEntry(@PathVariable long id) {
        return  logEntryDaoWrapper.removeLogEntry(id);
    }

    @RequestMapping(value = "/entries", method = DELETE)
    public RestResponse<Void> removeLogEntries(@RequestParam List<Long> entries) {
        return  logEntryDaoWrapper.removeLogEntries(entries);
    }
}
