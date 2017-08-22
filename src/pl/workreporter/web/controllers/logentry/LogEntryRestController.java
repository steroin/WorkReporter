package pl.workreporter.web.controllers.logentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.web.beans.entities.logentry.LogEntry;
import pl.workreporter.web.beans.entities.logentry.LogEntryDao;
import pl.workreporter.web.beans.entities.project.Project;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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

    @RequestMapping(value = "/entries/projects/{id}", method = GET)
    public @ResponseBody
    List<Project> getAllUsersProjects(@PathVariable("id") long userId) {
        return null;
    }

}
