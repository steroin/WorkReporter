package pl.workreporter.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
