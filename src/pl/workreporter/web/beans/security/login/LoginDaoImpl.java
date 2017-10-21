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
                "au.positionid, au.teamid " +
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

        List<Long> managedSolutions = getManagedSolutions(result.get("login").toString());
        List<Long> managedTeams = getManagedTeams(result.get("login").toString());
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new UserRole("ROLE_USER"));

        if (managedSolutions.size() > 0) {
            authorities.add(new UserRole("ROLE_SOLUTION_ADMIN"));
            authorities.add(new UserRole("ROLE_TEAM_ADMIN"));
        }
        if (managedTeams.size() > 0) {
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
        cud.setPositionId(result.get("teamid") == null ? null : Long.parseLong(result.get("teamid").toString()));
        cud.setEmail(result.get("email").toString());
        cud.setAccountNonExpired(true);
        cud.setAccountNonLocked(true);
        cud.setCredentialsNonExpired(true);
        cud.setEnabled(true);
        cud.setManagedSolutions(managedSolutions);
        cud.setManagedTeams(managedTeams);

        return cud;
    }

    @Override
    public List<Long> getManagedSolutions(String login) {
        return getManagedSolutions("login", "'"+login+"'");
    }

    @Override
    public List<Long> getManagedSolutionsByEmail(String email) {
        return getManagedSolutions("email", "'"+email+"'");
    }

    private List<Long> getManagedSolutions(String keyAttribute, Object value) {
        String query = "select sa.solutionid from solution_administrator sa inner join appuser au on sa.userid=au.id " +
                "inner join account ac on au.accountid=ac.id where "+keyAttribute+"="+value.toString();

        List<Long> managedSolutions = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        for (Map<String, Object> map : result) {
            managedSolutions.add(Long.parseLong(map.get("solutionid").toString()));
        }
        return managedSolutions;
    }

    @Override
    public List<Long> getManagedTeams(String login) {
        return getManagedTeams("login", "'"+login+"'");
    }

    @Override
    public List<Long> getManagedTeamsByEmail(String email) {
        return getManagedTeams("email", "'"+email+"'");
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
