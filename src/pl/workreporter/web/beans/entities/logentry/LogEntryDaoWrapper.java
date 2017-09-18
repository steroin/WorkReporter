package pl.workreporter.web.beans.entities.logentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.security.rest.*;
import pl.workreporter.web.service.security.PrincipalAuthenticator;

import java.util.List;

/**
 * Created by Sergiusz on 17.09.2017.
 */
@Repository
public class LogEntryDaoWrapper {
    @Autowired
    private LogEntryDao logEntryDao;
    @Autowired
    private PrincipalAuthenticator authenticator;

    public RestResponse<LogEntry> getLogEntryById(long id) {
        LogEntry logEntry;
        try {
            logEntry = logEntryDao.getLogEntryById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        if (authenticator.authenticateUserId(logEntry.getUser().getId()) ||
                authenticator.authenticateSolutionAdministrator(logEntry.getTeam().getSolution().getId()) ||
                authenticator.authenticateTeamAdministrator(logEntry.getTeam().getId())) {
            return new RestResponseSuccess<>(logEntry);
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<LogEntry> addLogEntry(long userId, String startDate, double loggedHours,
                                long logTypeId, Long projectId) {
        if (authenticator.authenticateUserId(userId)) {
            try {
                LogEntry logEntry = logEntryDao.addLogEntry(userId, startDate, loggedHours, logTypeId, projectId);
                return new RestResponseSuccess<>(logEntry);
            } catch (Exception e) {
                e.printStackTrace();
                return new RestResponseAdditionFailedError<>();
            }
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<List<LogEntry>> getDailyLogEntries(long userId, int year, int month, int day) {
        List<LogEntry> logEntries;

        try {
            logEntries = logEntryDao.getDailyLogEntries(userId, year, month, day);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        boolean authenticated = true;

        for (LogEntry logEntry : logEntries) {
            if (!authenticator.authenticateUserId(logEntry.getUser().getId()) &&
                    !authenticator.authenticateSolutionAdministrator(logEntry.getTeam().getSolution().getId()) &&
                    !authenticator.authenticateTeamAdministrator(logEntry.getTeam().getId())) {
                authenticated = false;
            }
        }

        if (!authenticated || !authenticator.authenticateUserId(userId)) {
            return new RestResponseAuthenticationError<>();
        }

        return new RestResponseSuccess<>(logEntries);
    }


    public RestResponse<List<LogEntry>> getLastLogEntries(long userId, int period) {
        List<LogEntry> logEntries;

        try {
            logEntries = logEntryDao.getLastLogEntries(userId, period);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        boolean authenticated = true;

        for (LogEntry logEntry : logEntries) {
            if (!authenticator.authenticateUserId(logEntry.getUser().getId()) &&
                    !authenticator.authenticateSolutionAdministrator(logEntry.getTeam().getSolution().getId()) &&
                    !authenticator.authenticateTeamAdministrator(logEntry.getTeam().getId())) {
                authenticated = false;
            }
        }

        if (!authenticated) {
            return new RestResponseAuthenticationError<>();
        }

        return new RestResponseSuccess<>(logEntries);
    }

    public RestResponse<List<LogType>> getAllLogTypes() {
        try {
            return new RestResponseSuccess<>(logEntryDao.getAllLogTypes());
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }


    public RestResponse<Void> changeLogEntryStatus(long logEntryId, int status) {
        LogEntry logEntry;
        try {
            logEntry = logEntryDao.getLogEntryById(logEntryId);

            if (authenticator.authenticateSolutionAdministrator(logEntry.getTeam().getSolution().getId()) ||
                    authenticator.authenticateTeamAdministrator(logEntry.getTeam().getId())) {
                logEntryDao.changeLogEntryStatus(logEntryId, status);
                return new RestResponseSuccess<>();
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseUpdateFailedError<>();
        }
    }


    public RestResponse<Void> updateLogEntry(LogEntry logEntry) {
        try {
            LogEntry entryFromDao = logEntryDao.getLogEntryById(logEntry.getId());

            if (authenticator.authenticateUserId(logEntry.getUser().getId()) ||
                    authenticator.authenticateSolutionAdministrator(logEntry.getTeam().getSolution().getId()) ||
                    authenticator.authenticateTeamAdministrator(logEntry.getTeam().getId()) &&
                    entryFromDao.getUser().getId() == logEntry.getUser().getId() &&
                    entryFromDao.getStatus() == logEntry.getStatus() &&
                    entryFromDao.getAcceptedBy().getId() == logEntry.getAcceptedBy().getId() &&
                    entryFromDao.getUser().getId() == logEntry.getUser().getId()) {
                logEntryDao.updateLogEntry(logEntry);
                return new RestResponseSuccess<>();
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseUpdateFailedError<>();
        }
    }

    public RestResponse<Void> removeLogEntry(long logEntryId) {
        LogEntry logEntry;
        try {
            logEntry = logEntryDao.getLogEntryById(logEntryId);

            if ((authenticator.authenticateUserId(logEntry.getUser().getId()) && logEntry.getStatus() == 1) ||
                    authenticator.authenticateSolutionAdministrator(logEntry.getTeam().getSolution().getId())) {
                logEntryDao.removeLogEntry(logEntryId);
                return new RestResponseSuccess<>();
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseRemovingFailedError<>();
        }
    }

    public RestResponse<Void> removeLogEntries(List<Long> entries) {
        List<LogEntry> logEntries;

        try {
            logEntries = logEntryDao.getLogEntries(entries);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        boolean authenticated = true;

        for (LogEntry logEntry : logEntries) {
            if ((!authenticator.authenticateUserId(logEntry.getUser().getId()) || logEntry.getStatus() != 1) &&
                    !authenticator.authenticateSolutionAdministrator(logEntry.getTeam().getSolution().getId())) {
                authenticated = false;
            }
        }
        if (!authenticated) {
            return new RestResponseAuthenticationError<>();
        }
        return new RestResponseSuccess<>();
    }
}
