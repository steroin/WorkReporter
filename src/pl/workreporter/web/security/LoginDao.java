package pl.workreporter.web.security;

/**
 * Created by Sergiusz on 09.08.2017.
 */
public interface LoginDao {
    void createAccount(String login, String password, String tempPassword, String email, int status);
    String getEmail(int id);
    String getEmail(String login);
    String getLogin(int id);
    String getPasswordHash(int id);
    String getPasswordHash(String login);
    String getPasswordHashByEmail(String email);
}
