package pl.workreporter.web.beans.entities.logentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.service.date.DateParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 21.08.2017.
 */
@Repository
public class LogEntryDaoImpl implements LogEntryDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private DateParser dateParser;

    @Override
    public void addLogEntry(long solutionId, long userId, String day, long teamId, String startHour, double loggedHours,
                            long logTypeId, Long projectId, int status) {
        String query = "insert into log_entry(id, userid, teamid, logtypeid, projectid, loggedhours, day, log_date, " +
                "last_edition_date, status, acceptedby, starthour) " +
                "values(logentryseq.nextval, "+userId+", "+teamId+", "+logTypeId+", "+projectId+", "+loggedHours+", " +
                dateParser.parseToDatabaseTimestamp(day)+", sysdate, sysdate, 0, null, "+startHour;
        jdbcTemplate.execute(query);
    }

    @Override
    public List<LogEntry> getDailyLogEntries(long userId, int year, int month, int day) {
        String query = "select id, userid, teamid, logtypeid, projectid, loggedhours, to_char(day, 'hh24;mi') as parsedday, " +
                "log_date, last_edition_date, status, acceptedby " +
                "from log_entry where userid="+userId+" and to_date(day)=to_date('"+day+"-"+month+"-"+year+"', 'dd-mm-yyyy')";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<LogEntry> entries = new ArrayList<>();

        for (Map<String, Object> map : result) {
            LogEntry logEntry = new LogEntry();
            logEntry.setId(Long.parseLong(map.get("id").toString()));
            logEntry.setUserId(Long.parseLong(map.get("userid").toString()));
            logEntry.setTeamId(Long.parseLong(map.get("teamid").toString()));
            logEntry.setLogTypeId(Long.parseLong(map.get("logtypeid").toString()));
            logEntry.setProjectId(map.get("projectid") == null ? null : Long.parseLong(map.get("projectid").toString()));
            logEntry.setLoggedHours(Double.parseDouble(map.get("loggedhours").toString()));
            logEntry.setDay(map.get("parsedday").toString());
            logEntry.setLogDate(dateParser.parseToReadableDate(map.get("log_date")));
            logEntry.setLastEditionDate(dateParser.parseToReadableDate(map.get("last_edition_date")));
            logEntry.setStatus(Integer.parseInt(map.get("status").toString()));
            logEntry.setAcceptedBy(map.get("acceptedby") == null ? null : Long.parseLong(map.get("acceptedby").toString()));
            entries.add(logEntry);
        }
        return entries;
    }
}
