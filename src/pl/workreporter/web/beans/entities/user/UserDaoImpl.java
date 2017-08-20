package pl.workreporter.web.beans.entities.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.service.date.DateParser;
import pl.workreporter.web.service.mail.MailNotificator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by Sergiusz on 19.08.2017.
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DateParser dateParser;

    @Override
    public User getUserById(long id) {
        String query = "select * " +
                "from appuser au " +
                "join personal_data pd on au.personaldataid=pd.id " +
                "join account ac on au.accountid=ac.id " +
                "where au.id="+id;
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        User user = new User();
        user.setId(Long.parseLong(result.get("id").toString()));
        user.setSolutionId(Long.parseLong(result.get("solutionid").toString()));
        user.setTeamId(result.get("teamid") == null ? null : Long.parseLong(result.get("teamid").toString()));
        user.setAccountId(Long.parseLong(result.get("accountid").toString()));
        user.setPositionId(Long.parseLong(result.get("positionid").toString()));
        user.setPersonalDataId(Long.parseLong(result.get("personaldataid").toString()));
        user.setWorkingTime(Double.parseDouble(result.get("working_time").toString()));
        user.setFirstName(result.get("firstname").toString());
        user.setLastName(result.get("lastname").toString());
        user.setBirthday(dateParser.parseToReadableDate(result.get("birthday")));
        user.setPhone(result.get("phone") == null ? null : result.get("phone").toString());
        user.setLogin(result.get("login").toString());
        user.setEmail(result.get("email").toString());
        user.setAccountStatus(Integer.parseInt(result.get("status").toString()));
        user.setCreationDate(dateParser.parseToReadableDate(result.get("creation_date")));
        user.setLastEditionDate(dateParser.parseToReadableDate(result.get("last_edition_date")));

        return user;
    }

    private List<User> getAllUsersWithAttribute(String attribute, String value) {
        String query = "select * " +
                "from appuser au " +
                "join personal_data pd on au.personaldataid=pd.id " +
                "join account ac on au.accountid=ac.id " +
                "where "+attribute+"="+value;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<User> usersList = new ArrayList<>();

        for (Map<String, Object> map : result) {
            User user = new User();
            user.setId(Long.parseLong(map.get("id").toString()));
            user.setSolutionId(Long.parseLong(map.get("solutionid").toString()));
            user.setTeamId(map.get("accountid") == null ? null : Long.parseLong(map.get("accountid").toString()));
            user.setAccountId(Long.parseLong(map.get("accountid").toString()));
            user.setPositionId(Long.parseLong(map.get("positionid").toString()));
            user.setPersonalDataId(Long.parseLong(map.get("personaldataid").toString()));
            user.setWorkingTime(Double.parseDouble(map.get("working_time").toString()));
            user.setFirstName(map.get("firstname").toString());
            user.setLastName(map.get("lastname").toString());
            user.setBirthday(dateParser.parseToReadableDate(map.get("birthday")));
            user.setPhone(map.get("phone") == null ? null : map.get("phone").toString());
            user.setLogin(map.get("login").toString());
            user.setEmail(map.get("email").toString());
            user.setAccountStatus(Integer.parseInt(map.get("status").toString()));
            user.setCreationDate(dateParser.parseToReadableDate(map.get("creation_date")));
            user.setLastEditionDate(dateParser.parseToReadableDate(map.get("last_edition_date")));
            usersList.add(user);
        }
        return usersList;
    }
    @Override
    public List<User> getAllUsersInSolution(long solutionId) {
        return getAllUsersWithAttribute("solutionid", solutionId+"");
    }

    @Override
    public List<User> getAllUsersInTeam(long teamId) {
        return getAllUsersWithAttribute("teamid", teamId+"");
    }

    @Override
    public User addUser(long solutionId, Long teamId, long positionId, double workingTime, String firstName,
                        String lastName, String birthday, String phone, String login, String password, String email) {
        String query = "select appuserseq.nextval as usernextval, accountseq.nextval as accountnextval, personaldataseq.nextval as pdnextval from dual";
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        long userId = Long.parseLong(result.get("usernextval").toString());
        long accountId = Long.parseLong(result.get("accountnextval").toString());
        long personalDataId = Long.parseLong(result.get("pdnextval").toString());

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(16));

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("BEGIN \n");
        queryBuilder.append("insert into personal_data(id, firstname, lastname, birthday, phone) " +
                "values("+personalDataId+", '"+firstName+"', '"+lastName+"', "+dateParser.parseToDatabaseTimestamp(birthday)+", '"+phone+"');\n");
        queryBuilder.append("insert into account(id, login, password, email, status) " +
                "values("+accountId+", '"+login+"', '"+hashedPassword+"', '"+email+"', 1);\n");

        queryBuilder.append("insert into appuser(id, solutionid, teamid, accountid, positionid, personaldataid, " +
                "working_time, creation_date, last_edition_date) " +
                "values("+userId+", "+solutionId+", "+teamId+", "+accountId+", "+positionId+", "+personalDataId+", " +
                ""+workingTime+", sysdate, sysdate);\n");
        queryBuilder.append("END;");
        jdbcTemplate.execute(queryBuilder.toString());
        return getUserById(userId);
    }

    @Override
    public void removeUser(long solutionId, long id) {
        String query = "delete from appuser where id="+id;
        jdbcTemplate.execute(query);
    }

    @Override
    public void removeUsers(long solutionId, List<Long> ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from appuser where solutionid="+solutionId);
        queryBuilder.append(" and (");
        for (int i = 0; i < ids.size(); i++) {
            queryBuilder.append("id="+ids.get(i));
            if (i < ids.size() - 1) {
                queryBuilder.append(" or ");
            }
        }
        queryBuilder.append(")");
        jdbcTemplate.execute(queryBuilder.toString());
    }

    @Override
    public void updateUser(User user) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("BEGIN \n");
        queryBuilder.append("update personal_data set firstname='"+user.getFirstName()+"', lastname='"+user.getLastName()+"', " +
                "birthday="+dateParser.parseToDatabaseTimestamp(user.getBirthday())+", phone='"+user.getPhone()+"' " +
                "where id="+user.getPersonalDataId()+"; \n");

        queryBuilder.append("update account set login='"+user.getLogin()+"', email='"+user.getEmail()+"', " +
                "status="+user.getAccountStatus()+" where id="+user.getAccountId()+"; \n");

        queryBuilder.append("update appuser set solutionid="+user.getSolutionId()+", teamid="+user.getTeamId()+", " +
                "accountid="+user.getAccountId()+", positionid="+user.getPositionId()+", personaldataid="+user.getPersonalDataId()+", " +
                "working_time="+user.getWorkingTime()+", creation_date="+dateParser.parseToDatabaseTimestamp(user.getCreationDate())+", " +
                "last_edition_date=sysdate where id="+user.getId()+"; \n");
        queryBuilder.append("END;");
        jdbcTemplate.execute(queryBuilder.toString());
    }

    @Override
    public void changePassword(long id, String newPassword) {
        String query = "select accountid from appuser where id="+id;
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        long accountId = Long.parseLong(result.get("accountid").toString());
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(16));
        query = "update account set password=? where id=?";
        jdbcTemplate.update(query, hashedPassword, accountId);
    }
}
