package pl.workreporter.web.beans.security.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import pl.workreporter.security.authentication.CompleteUserDetails;
import pl.workreporter.security.authentication.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 09.08.2017.
 */

@Repository
public class LoginDaoImpl implements LoginDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createAccount(String login, String password, String tempPassword, String email, int status) {
        jdbcTemplate.execute("insert into account(id, login, password, temppassword, email, status) \n"+
                "values (accountseq.nextval, '"+login+"', '"+password+"', '"+tempPassword+"', '"+email+"', "+status+")");
    }

    @Override
    public String getEmail(long id) {
        return getEmail("id", id);
    }

    @Override
    public String getEmail(String login) {
        return getEmail("login", "'"+login+"'");
    }

    private String getEmail(String keyAttribute, Object value) {
        String query = "select email from account where "+keyAttribute+"="+value.toString();
        String login = jdbcTemplate.queryForObject(query, String.class);
        return login;
    }

    @Override
    public String getLogin(long id) {
        return getLogin("id", id);
    }

    private String getLogin(String keyAttribute, Object value) {
        String query = "select login from account where "+keyAttribute+"="+value.toString();
        String login = jdbcTemplate.queryForObject(query, String.class);
        return login;
    }

    @Override
    public String getPasswordHash(long id) {
        return getPasswordHash("au.id", id);
    }

    @Override
    public String getPasswordHash(String login) {
        return getPasswordHash("login", "'"+login+"'");
    }

    @Override
    public String getPasswordHashByEmail(String email) {
        return getPasswordHash("email", "'"+email+"'");
    }

    private String getPasswordHash(String keyAttribute, Object value) {
        String query = "select password from account ac join appuser au on ac.id=au.accountid where "+keyAttribute+"="+value.toString();
        String password = jdbcTemplate.queryForObject(query, String.class);
        return password;
    }

    @Override
    public CompleteUserDetails loadUserDetails(String login) {
        return loadUserDetails("login", "'"+login+"'");
    }

    @Override
    public CompleteUserDetails loadUserDetailsByEmail(String email) {
        return loadUserDetails("email", "'"+email+"'");
    }

    private CompleteUserDetails loadUserDetails(String keyAttribute, Object value) {
        String query = "select au.id as userId, ac.id as accountId, au.solutionid, firstname, lastname, login, password, ac.email as email, " +
                "au.positionid, au.teamid, au.is_solution_manager " +
                "from account ac " +
                "inner join appuser au on ac.id = au.accountid " +
                "inner join personal_data pd on au.personaldataid = pd.id " +
                "where "+keyAttribute+"="+value.toString();

        Map<String, Object> result;

        try {
            result = jdbcTemplate.queryForMap(query);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new UserRole("ROLE_USER"));

        boolean solutionManager = "1".equals(result.get("is_solution_manager").toString());
        if (solutionManager) {
            authorities.add(new UserRole("ROLE_SOLUTION_ADMIN"));
            authorities.add(new UserRole("ROLE_TEAM_ADMIN"));
        }
        Long teamId = result.get("teamid") == null ? null : Long.parseLong(result.get("teamid").toString());
        boolean teamLeader = isTeamLeader(teamId, Long.parseLong(result.get("userId").toString()));
        if (teamId != null && teamLeader) {
            authorities.add(new UserRole("ROLE_TEAM_ADMIN"));
        }

        CompleteUserDetails cud = new CompleteUserDetails();
        cud.setAuthorities(authorities);
        cud.setAccountId(Long.parseLong(result.get("accountId").toString()));
        cud.setUserId(Long.parseLong(result.get("userId").toString()));
        cud.setSolutionId(Long.parseLong(result.get("solutionid").toString()));
        cud.setFirstName(result.get("firstname").toString());
        cud.setLastName(result.get("lastname").toString());
        cud.setPassword(result.get("password").toString());
        cud.setUsername(result.get("login").toString());
        cud.setPositionId(result.get("positionid") == null ? null : Long.parseLong(result.get("positionid").toString()));
        cud.setTeamId(teamId);
        cud.setEmail(result.get("email").toString());
        cud.setSolutionManager(solutionManager);
        cud.setTeamManager(teamLeader);
        cud.setAccountNonExpired(true);
        cud.setAccountNonLocked(true);
        cud.setCredentialsNonExpired(true);
        cud.setEnabled(true);

        return cud;
    }

    private boolean isTeamLeader(long teamId, long userId) {
        String query = "select leaderid from team where id="+teamId;
        Map<String, Object> result;

        try {
            result = jdbcTemplate.queryForMap(query);
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }

        return Long.parseLong(result.get("leaderid").toString()) == userId;
    }

    @Override
    public void changeUserPassword(long id, String hashedPassword) {
        String query = "select accountid from appuser where id="+id;
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        long accountId = Long.parseLong(result.get("accountid").toString());
        query = "update account set password=? where id=?";
        jdbcTemplate.update(query, hashedPassword, accountId);
    }

    private List<Long> getManagedTeams(String keyAttribute, Object value) {
        String query = "select t.id as teamid from team t inner join appuser au on t.leaderid=au.id " +
                "inner join account ac on au.accountid=ac.id where "+keyAttribute+"="+value.toString();

        List<Long> managedTeams = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        for (Map<String, Object> map : result) {
            managedTeams.add(Long.parseLong(map.get("teamid").toString()));
        }
        return managedTeams;
    }
}
