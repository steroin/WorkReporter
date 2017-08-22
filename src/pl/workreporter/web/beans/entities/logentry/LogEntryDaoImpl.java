package pl.workreporter.web.beans.entities.logentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.service.date.DateParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LogEntryDaoImpl implements LogEntryDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private DateParser dateParser;

    @Override
    public LogEntry getLogEntryById(long id) {
        String query = "select le.id, le.userid, le.teamid, le.logtypeid, le.projectid, le.loggedhours, to_char(le.day, 'dd-mm-yyyy') as parsedday, " +
        "le.log_date, le.last_edition_date, le.status, le.acceptedby, to_char(le.starthour, 'hh24:mi') as parsedstarthour, " +
                "p.name as projectname, lt.name as logtypename " +
                "from log_entry le " +
                "left join project p on le.projectid=p.id " +
                "join log_type lt on le.logtypeid=lt.id " +
                "where le.id="+id;

        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        LogEntry logEntry = new LogEntry();
        logEntry.setId(Long.parseLong(result.get("id").toString()));
        logEntry.setUserId(Long.parseLong(result.get("userid").toString()));
        logEntry.setTeamId(Long.parseLong(result.get("teamid").toString()));
        logEntry.setLogTypeId(Long.parseLong(result.get("logtypeid").toString()));
        logEntry.setProjectId(result.get("projectid") == null ? null : Long.parseLong(result.get("projectid").toString()));
        logEntry.setLoggedHours(Double.parseDouble(result.get("loggedhours").toString()));
        logEntry.setDay(result.get("parsedday").toString());
        logEntry.setLogDate(dateParser.parseToReadableDate(result.get("log_date")));
        logEntry.setLastEditionDate(dateParser.parseToReadableDate(result.get("last_edition_date")));
        logEntry.setStatus(Integer.parseInt(result.get("status").toString()));
        logEntry.setAcceptedBy(result.get("acceptedby") == null ? null : Long.parseLong(result.get("acceptedby").toString()));
        logEntry.setStartHour(result.get("parsedstarthour").toString());
        logEntry.setProjectName(result.get("projectname") == null ? null : result.get("projectname").toString());
        logEntry.setLogTypeName(result.get("logtypename") == null ? null : result.get("logtypename").toString());
        return logEntry;
    }

    @Override
    public LogEntry addLogEntry(long userId, String day, String startHour, double loggedHours,
                            long logTypeId, Long projectId) {
        String query = "select teamid from appuser where id = "+userId;
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        String teamId = result.get("teamid") == null ? "null" : result.get("teamid").toString();
        query = "select logentryseq.nextval as id from dual";
        long id = Long.parseLong(jdbcTemplate.queryForMap(query).get("id").toString());
        query = "insert into log_entry(id, userid, teamid, logtypeid, projectid, loggedhours, day, log_date, " +
                "last_edition_date, status, acceptedby, starthour) " +
                "values("+id+", "+userId+", "+teamId+", "+logTypeId+", "+projectId+", "+loggedHours+", " +
                dateParser.parseToDatabaseTimestamp(day)+", sysdate, sysdate, 1, null, to_timestamp('"+startHour+"', 'hh24:mi'))";
        jdbcTemplate.execute(query);
        return getLogEntryById(id);
    }

    @Override
    public List<LogEntry> getDailyLogEntries(long userId, int year, int month, int day) {
        String query = "select le.id, le.userid, le.teamid, le.logtypeid, le.projectid, le.loggedhours, to_char(le.day, 'dd-mm-yyyy') as parsedday, " +
                "le.log_date, le.last_edition_date, le.status, le.acceptedby, to_char(le.starthour, 'hh24:mi') as parsedstarthour, " +
                "p.name as projectname, lt.name as logtypename " +
                "from log_entry le " +
                "left join project p on le.projectid=p.id " +
                "join log_type lt on le.logtypeid=lt.id " +
                "where le.userid="+userId+" and to_date(le.day)=to_date('"+day+"-"+month+"-"+year+"', 'dd-mm-yyyy')";
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
            logEntry.setStartHour(map.get("parsedstarthour").toString());
            logEntry.setProjectName(map.get("projectname") == null ? null : map.get("projectname").toString());
            logEntry.setLogTypeName(map.get("logtypename") == null ? null : map.get("logtypename").toString());
            entries.add(logEntry);
        }
        return entries;
    }

    @Override
    public List<LogType> getAllLogTypes() {
        String query = "select * from log_type";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        List<LogType> types = new ArrayList<>();

        for (Map<String, Object> map : result) {
            LogType type = new LogType();
            type.setId(Long.parseLong(map.get("id").toString()));
            type.setName(map.get("name").toString());
            types.add(type);
        }
        return types;
    }

    @Override
    public void updateLogEntry(LogEntry logEntry) {
        String query = "update log_entry set " +
                "userid="+logEntry.getUserId()+", " +
                "teamid="+logEntry.getTeamId()+", "+
                "logtypeid="+logEntry.getLogTypeId()+", "+
                "projectid="+logEntry.getProjectId()+", "+
                "loggedhours="+logEntry.getLoggedHours()+", "+
                "day=to_timestamp('"+logEntry.getDay()+"', 'dd-mm-yyyy'), "+
                "log_date="+dateParser.parseToDatabaseTimestamp(logEntry.getLogDate())+", "+
                "last_edition_date=sysdate, "+
                "status="+logEntry.getStatus()+", "+
                "starthour=to_timestamp('"+logEntry.getStartHour()+"', 'hh24:mi') " +
                "where id="+logEntry.getId();
        jdbcTemplate.execute(query);
    }
}
