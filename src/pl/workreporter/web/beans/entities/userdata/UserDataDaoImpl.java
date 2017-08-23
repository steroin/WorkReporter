package pl.workreporter.web.beans.entities.userdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.service.date.DateParser;

import java.util.Map;

/**
 * Created by Sergiusz on 23.08.2017.
 */
@Repository
public class UserDataDaoImpl implements UserDataDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DateParser dateParser;

    @Override
    public UserData getUserData(long userId) {
        String query = "select pd.firstname, pd.lastname, pd.birthday, pd.phone, " +
                "ac.login, ac.email, s.name as solutionname, t.name as teamname, " +
                "p.name as positionname, au.working_time " +
                "from appuser au " +
                "join personal_data pd on au.personaldataid=pd.id " +
                "join account ac on au.accountid=ac.id " +
                "join solution s on au.solutionid=s.id " +
                "join team t on au.teamid=t.id " +
                "join position p on au.positionid=p.id " +
                "where au.id="+userId;

        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        UserData userData = new UserData();

        userData.setUserId(userId);
        userData.setFirstName(result.get("firstname").toString());
        userData.setLastName(result.get("lastname").toString());
        userData.setBirthday(dateParser.parseToReadableDate(result.get("birthday").toString()));
        userData.setPhone(result.get("phone") == null ? null : result.get("phone").toString());
        userData.setLogin(result.get("login").toString());
        userData.setEmail(result.get("email").toString());
        userData.setSolutionName(result.get("solutionname").toString());
        userData.setTeamName(result.get("teamname").toString());
        userData.setPositionName(result.get("positionname").toString());
        userData.setWorkingTime(Double.parseDouble(result.get("working_time").toString()));
        return userData;
    }
}
