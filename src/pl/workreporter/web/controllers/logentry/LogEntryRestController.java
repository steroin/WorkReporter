package pl.workreporter.web.controllers.logentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.logentry.LogEntry;
import pl.workreporter.web.beans.entities.logentry.LogEntryDao;
import pl.workreporter.web.beans.entities.logentry.LogType;
import pl.workreporter.web.beans.entities.project.Project;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Sergiusz on 21.08.2017.
 */
@RestController
public class LogEntryRestController {
    @Autowired
    private LogEntryDao logEntryDao;

    @RequestMapping(value = "/entries", method = GET)
    public @ResponseBody
    List<LogEntry> getAllLogEntries(@RequestParam("userid") long userId,
                                    @RequestParam("year") int year,
                                    @RequestParam("month") int month,
                                    @RequestParam("day") int day) {
        List<LogEntry> result = logEntryDao.getDailyLogEntries(userId, year, month, day);
        return result;
    }

    @RequestMapping(value = "/entrytypes", method = GET)
    public @ResponseBody
    List<LogType> getLogEntryTypes() {
        List<LogType> result = logEntryDao.getAllLogTypes();
        return result;
    }

    @RequestMapping(value = "/entries", method = POST)
    public LogEntry addLogEntry(@RequestBody Map<String, String> logEntry) {
        return logEntryDao.addLogEntry(Long.parseLong(logEntry.get("userid")),
                logEntry.get("day"),
                logEntry.get("starthour"),
                Double.parseDouble(logEntry.get("loggedhours")),
                Long.parseLong(logEntry.get("logtypeid")),
                logEntry.get("projectid") == null || logEntry.get("projectid").isEmpty() ? null : Long.parseLong(logEntry.get("projectid")));
    }
}
