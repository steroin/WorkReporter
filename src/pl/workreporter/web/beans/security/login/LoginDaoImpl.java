package pl.workreporter.web.beans.security.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import pl.workreporter.security.login.CompleteUserDetails;
import pl.workreporter.security.login.UserRole;

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
    public String getEmail(int id) {
        String query = "select email from account where id="+id;
        try {
            String email = jdbcTemplate.queryForObject(query, String.class);
            return email;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String getEmail(String login) {
        String query = "select email from account where login='"+login+"'";
        try {
            String email = jdbcTemplate.queryForObject(query, String.class);
            return email;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String getLogin(int id) {
        String query = "select login from account where id="+id;
        try {
            String login = jdbcTemplate.queryForObject(query, String.class);
            return login;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String getPasswordHash(int id) {
        String query = "select password from account where id="+id;
        try {
            String password = jdbcTemplate.queryForObject(query, String.class);
            return password;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String getPasswordHash(String login) {
        String query = "select password from account where login='"+login+"'";
        try {
            String password = jdbcTemplate.queryForObject(query, String.class);
            return password;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String getPasswordHashByEmail(String email) {
        String query = "select password from account where email='"+email+"'";
        try {
            String password = jdbcTemplate.queryForObject(query, String.class);
            return password;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public CompleteUserDetails loadUserDetails(String login) {
        String query = "select login, password, email from account where login='"+login+"'";
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new UserRole("ROLE_USER"));

            CompleteUserDetails cud = new CompleteUserDetails();
            cud.setAuthorities(authorities);
            cud.setPassword(result.get("password").toString());
            cud.setUsername(result.get("login").toString());
            cud.setEmail(result.get("email").toString());
            cud.setAccountNonExpired(true);
            cud.setAccountNonLocked(true);
            cud.setCredentialsNonExpired(true);
            cud.setEnabled(true);
            cud.setManagedSolutions(getManagedSolutions(login));
            cud.setManagedTeams(getManagedTeams(login));

            return cud;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public CompleteUserDetails loadUserDetailsByEmail(String email) {
        String query = "select login, password, email from account where email='"+email+"'";
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new UserRole("ROLE_USER"));

            CompleteUserDetails cud = new CompleteUserDetails();
            cud.setAuthorities(authorities);
            cud.setPassword(result.get("password").toString());
            cud.setUsername(result.get("login").toString());
            cud.setEmail(result.get("email").toString());
            cud.setAccountNonExpired(true);
            cud.setAccountNonLocked(true);
            cud.setCredentialsNonExpired(true);
            cud.setEnabled(true);
            cud.setManagedSolutions(getManagedSolutionsByEmail(email));
            cud.setManagedTeams(getManagedTeamsByEmail(email));

            return cud;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Integer> getManagedSolutions(String login) {
        String query = "select sa.solutionid from solution_administrator sa inner join appuser au on sa.userid=au.id " +
                "inner join account ac on au.accountid=ac.id where ac.login='"+login+"'";

        List<Integer> managedSolutions = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        for (Map<String, Object> map : result) {
            managedSolutions.add(Integer.parseInt(map.get("solutionid").toString()));
        }
        return managedSolutions;
    }

    @Override
    public List<Integer> getManagedSolutionsByEmail(String email) {
        String query = "select sa.solutionid from solution_administrator sa inner join appuser au on sa.userid=au.id " +
                "inner join account ac on au.accountid=ac.id where ac.email='"+email+"'";

        List<Integer> managedSolutions = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        for (Map<String, Object> map : result) {
            managedSolutions.add(Integer.parseInt(map.get("solutionid").toString()));
        }
        return managedSolutions;
    }

    @Override
    public List<Integer> getManagedTeams(String login) {
        String query = "select ta.teamid from team_administrator ta inner join appuser au on ta.userid=au.id " +
                "inner join account ac on au.accountid=ac.id where ac.login='"+login+"'";

        List<Integer> managedTeams = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        for (Map<String, Object> map : result) {
            managedTeams.add(Integer.parseInt(map.get("teamid").toString()));
        }
        return managedTeams;
    }

    @Override
    public List<Integer> getManagedTeamsByEmail(String email) {
        String query = "select ta.teamid from team_administrator ta inner join appuser au on ta.userid=au.id " +
                "inner join account ac on au.accountid=ac.id where ac.email='"+email+"'";

        List<Integer> managedTeams = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        for (Map<String, Object> map : result) {
            managedTeams.add(Integer.parseInt(map.get("teamid").toString()));
        }
        return managedTeams;
    }
}
