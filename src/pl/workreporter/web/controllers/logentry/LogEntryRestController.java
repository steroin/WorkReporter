package pl.workreporter.web.controllers.logentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.logentry.LogEntry;
import pl.workreporter.web.beans.entities.logentry.LogEntryDao;
import pl.workreporter.web.beans.entities.logentry.LogType;
import pl.workreporter.web.beans.entities.position.Position;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Sergiusz on 21.08.2017.
 */
@RestController
public class LogEntryRestController {
    @Autowired
    private LogEntryDao logEntryDao;

    @RequestMapping(value = "/entries", method = GET)
    public @ResponseBody
    RestResponse<List<LogEntry>> getAllLogEntries(@RequestParam("userid") long userId,
                                                 @RequestParam("year") int year,
                                                 @RequestParam("month") int month,
                                                 @RequestParam("day") int day) {
        List<LogEntry> result = logEntryDao.getDailyLogEntries(userId, year, month, day);
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/entrytypes", method = GET)
    public @ResponseBody
    RestResponse<List<LogType>> getLogEntryTypes() {
        List<LogType> result = logEntryDao.getAllLogTypes();
        return new RestResponseSuccess<>(result);
    }

    @RequestMapping(value = "/entries", method = POST)
    public RestResponse<LogEntry> addLogEntry(@RequestBody Map<String, String> logEntry) {
        return new RestResponseSuccess<>(logEntryDao.addLogEntry(Long.parseLong(logEntry.get("userid")),
                logEntry.get("start"),
                Double.parseDouble(logEntry.get("loggedhours")),
                Long.parseLong(logEntry.get("logtypeid")),
                logEntry.get("projectid") == null || logEntry.get("projectid").isEmpty() ? null : Long.parseLong(logEntry.get("projectid"))));
    }

    @RequestMapping(value = "/entries/{id}", method = PATCH)
    public RestResponse<Void> updateLogEntry(@PathVariable("id") long entryId, @RequestBody LogEntry logEntry) {
        logEntryDao.updateLogEntry(logEntry);
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/entries/{id}", method = DELETE)
    public RestResponse<Void> removeLogEntry(@PathVariable long id) {
        logEntryDao.removeLogEntry(id);
        return new RestResponseSuccess<>();
    }

    @RequestMapping(value = "/entries", method = DELETE)
    public RestResponse<Void> removeLogEntries(@RequestParam List<Long> entries) {
        logEntryDao.removeLogEntries(entries);
        return new RestResponseSuccess<>();
    }
}
