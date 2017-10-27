package pl.workreporter.web.beans.entities.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.service.date.DateParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 27.10.2017.
 */
@Repository
public class ReportDaoImpl implements ReportDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DateParser dateParser;

    @Override
    public Report generateReport(ReportObject object, ReportSortingObject sortBy, ReportSortingType sortType, Date from, Date to, Long solutionId) {
        Report report = null;

        switch (object) {
            case EMPLOYEE:
                report = generateEmployeeReport(sortBy, sortType, from, to, solutionId);
                break;
            case PROJECT:
                report = generateProjectReport(sortBy, sortType, from, to, solutionId);
                break;
            case POSITION:
                report = generatePositionReport(sortBy, sortType, from, to, solutionId);
                break;
            case TEAM:
                report = generateTeamReport(sortBy, sortType, from, to, solutionId);
                break;
            default:
                report = generateEmployeeReport(sortBy, sortType, from, to, solutionId);
                break;
        }

        return report;
    }

    private Report generateEmployeeReport(ReportSortingObject sortBy, ReportSortingType sortType, Date from, Date to, Long solutionId) {
        String sortByKeyword = "";
        switch (sortBy) {
            case EMPLOYEE_SURNAME:
                sortByKeyword = "pd.lastname";
                break;
            case EMPLOYEE_FIRSTNAME:
                sortByKeyword = "pd.firstname";
                break;
            case EMPLOYEE_LOGIN:
                sortByKeyword = "ac.login";
                break;
            case EMPLOYEE_EMAIL:
                sortByKeyword = "ac.email";
                break;
            case EMPLOYEE_TEAM:
                sortByKeyword = "teamname";
                break;
            case EMPLOYEE_POSITION:
                sortByKeyword = "positionname";
                break;
            case EMPLOYEE_LOGGED_HOURS:
                sortByKeyword = "loggedhours";
                break;
            default:
                sortByKeyword = "pd.lastname";
        }
        String sortyTypeKeyword = "";
        switch (sortType) {
            case ASCENDING:
                sortyTypeKeyword = "asc";
                break;
            case DESCENDING:
                sortyTypeKeyword = "desc";
                break;
            default:
                sortByKeyword = "asc";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String stringDateFrom = simpleDateFormat.format(from);
        String stringDateTo = simpleDateFormat.format(to);

        String query = "select pd.firstname, pd.lastname, ac.login, ac.email, t.name as teamname, p.name as positionname, nvl(sum(le.loggedhours), 0) as loggedhours from appuser au " +
                "left join personal_data pd on au.personaldataid = pd.id " +
                "left join account ac on au.accountid = ac.id " +
                "left join team t on au.teamid = t.id " +
                "left join position p on au.positionid = p.id " +
                "left join " +
                "(select * from log_entry " +
                "where log_start between " + dateParser.parseToDatabaseTimestamp(stringDateFrom) + " and " + dateParser.parseToDatabaseTimestamp(stringDateTo) + ") " +
                "le on le.userid = au.id " +
                "where au.solutionid = "+solutionId+" " +
                "group by  au.id, pd.firstname, pd.lastname, ac.login, ac.email, t.name, p.name " +
                "order by " + sortByKeyword + " " +sortyTypeKeyword;

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query);
        Report report = new Report("", "Imię", "Nazwisko", "Login", "Email", "Zespół", "Stanowisko", "Przepracowane godziny");

        int count = 1;

        for (Map<String, Object> row : resultList) {
            String firstName = row.get("firstname").toString();
            String surname = row.get("lastname").toString();
            String login = row.get("login").toString();
            String email = row.get("email").toString();
            String teamName = row.get("teamname") == null ? "" : row.get("teamname").toString();
            String positionName = row.get("positionname") == null ? "" : row.get("positionname").toString();
            String loggedHours = row.get("loggedhours").toString();
            report.addRow(count+"", firstName, surname, login, email, teamName, positionName, loggedHours);
            count++;
        }
        return report;
    }

    private Report generateProjectReport(ReportSortingObject sortBy, ReportSortingType sortType, Date from, Date to, Long solutionId) {
        String sortByKeyword = "";
        switch (sortBy) {
            case PROJECT_NAME:
                sortByKeyword = "p.name";
                break;
            case PROJECT_LOGGED_HOURS:
                sortByKeyword = "loggedhours";
                break;
            default:
                sortByKeyword = "p.name";
        }
        String sortyTypeKeyword = "";

        switch (sortType) {
            case ASCENDING:
                sortyTypeKeyword = "asc";
                break;
            case DESCENDING:
                sortyTypeKeyword = "desc";
                break;
            default:
                sortByKeyword = "asc";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String stringDateFrom = simpleDateFormat.format(from);
        String stringDateTo = simpleDateFormat.format(to);

        String query = "select p.name, nvl(sum(le.loggedhours), 0) as loggedhours from project p " +
                "left join " +
                "(select * from log_entry " +
                "where log_start between " + dateParser.parseToDatabaseTimestamp(stringDateFrom) + " and " + dateParser.parseToDatabaseTimestamp(stringDateTo) + ") " +
                "le on le.projectid = p.id " +
                "where p.solutionid = "+solutionId+" " +
                "group by p.name " +
                "order by " + sortByKeyword + " " +sortyTypeKeyword;

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query);
        Report report = new Report("", "Nazwa projektu", "Przepracowane godziny");

        int count = 1;

        for (Map<String, Object> row : resultList) {
            String name = row.get("name").toString();
            String loggedHours = row.get("loggedhours").toString();
            report.addRow(count+"", name, loggedHours);
            count++;
        }
        return report;
    }

    private Report generatePositionReport(ReportSortingObject sortBy, ReportSortingType sortType, Date from, Date to, Long solutionId) {
        String sortByKeyword = "";
        switch (sortBy) {
            case POSITION_NAME:
                sortByKeyword = "p.name";
                break;
            case POSITION_LOGGED_HOURS:
                sortByKeyword = "loggedhours";
                break;
            default:
                sortByKeyword = "p.name";
        }
        String sortyTypeKeyword = "";

        switch (sortType) {
            case ASCENDING:
                sortyTypeKeyword = "asc";
                break;
            case DESCENDING:
                sortyTypeKeyword = "desc";
                break;
            default:
                sortByKeyword = "asc";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String stringDateFrom = simpleDateFormat.format(from);
        String stringDateTo = simpleDateFormat.format(to);

        String query = "select p.name, nvl(sum(le.loggedhours), 0) as loggedhours from position p " +
                "left join appuser au on au.positionid = p.id " +
                "left join " +
                "(select * from log_entry " +
                "where log_start between " + dateParser.parseToDatabaseTimestamp(stringDateFrom) + " and " + dateParser.parseToDatabaseTimestamp(stringDateTo) + ") " +
                "le on le.userid = au.id " +
                "where p.solutionid = "+solutionId+" " +
                "group by p.name " +
                "order by " + sortByKeyword + " " +sortyTypeKeyword;

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query);
        Report report = new Report("", "Nazwa stanowiska", "Przepracowane godziny");

        int count = 1;

        for (Map<String, Object> row : resultList) {
            String name = row.get("name").toString();
            String loggedHours = row.get("loggedhours").toString();
            report.addRow(count+"", name, loggedHours);
            count++;
        }
        return report;
    }

    private Report generateTeamReport(ReportSortingObject sortBy, ReportSortingType sortType, Date from, Date to, Long solutionId) {
        String sortByKeyword = "";
        switch (sortBy) {
            case TEAM_NAME:
                sortByKeyword = "t.name";
                break;
            case TEAM_LEADER:
            sortByKeyword = "pd.lastname";
                break;
            case TEAM_LOGGED_HOURS:
                sortByKeyword = "loggedhours";
                break;
            default:
                sortByKeyword = "t.name";
        }
        String sortyTypeKeyword = "";

        switch (sortType) {
            case ASCENDING:
                sortyTypeKeyword = "asc";
                break;
            case DESCENDING:
                sortyTypeKeyword = "desc";
                break;
            default:
                sortByKeyword = "asc";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String stringDateFrom = simpleDateFormat.format(from);
        String stringDateTo = simpleDateFormat.format(to);

        String query = "select t.name, pd.firstname, pd.lastname, nvl(sum(le.loggedhours), 0) as loggedhours from team t " +
                "left join appuser au on au.id = t.leaderid " +
                "left join personal_data pd on au.personaldataid = pd.id " +
                "left join " +
                "(select * from log_entry " +
                "where log_start between " + dateParser.parseToDatabaseTimestamp(stringDateFrom) + " and " + dateParser.parseToDatabaseTimestamp(stringDateTo) + ") " +
                "le on le.teamid = t.id " +
                "where t.solutionid = "+solutionId+" " +
                "group by t.name, pd.firstname, pd.lastname " +
                "order by " + sortByKeyword + " " +sortyTypeKeyword;

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query);
        Report report = new Report("", "Nazwa zespołu", "Lider zespołu", "Przepracowane godziny");

        int count = 1;

        for (Map<String, Object> row : resultList) {
            String name = row.get("name").toString();
            String leaderName = (row.get("firstname") == null ? "" : row.get("firstname").toString()) +
                    " " + (row.get("lastname") == null ? "" : row.get("lastname").toString());
            String loggedHours = row.get("loggedhours").toString();
            report.addRow(count+"", name, leaderName, loggedHours);
            count++;
        }
        return report;
    }
}
