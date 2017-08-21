package pl.workreporter.web.beans.entities.logentry;

import java.util.List;

/**
 * Created by Sergiusz on 21.08.2017.
 */
public interface LogEntryDao {
    void addLogEntry(long solutionId, long userId, String day, long teamId, String startHour, double loggedHours,
                     long logTypeId, Long projectId, int status);
    List<LogEntry> getDailyLogEntries(long userId, int year, int month, int day);
}
