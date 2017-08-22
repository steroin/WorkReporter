package pl.workreporter.web.beans.entities.logentry;

import java.util.List;

/**
 * Created by Sergiusz on 21.08.2017.
 */
public interface LogEntryDao {
    LogEntry getLogEntryById(long id);
    LogEntry addLogEntry(long userId, String day, String startHour, double loggedHours,
                    long logTypeId, Long projectId);
    List<LogEntry> getDailyLogEntries(long userId, int year, int month, int day);
    List<LogType> getAllLogTypes();
}
