package pl.workreporter.web.beans.security.login;

import pl.workreporter.security.authentication.CompleteUserDetails;

import java.util.List;

/**
 * Created by Sergiusz on 09.08.2017.
 */
public interface LoginDao {
    void createAccount(String login, String password, String tempPassword, String email, int status);
    String getEmail(long id);
    String getEmail(String login);
    String getLogin(long id);
    String getPasswordHash(long id);
    String getPasswordHash(String login);
    String getPasswordHashByEmail(String email);
    CompleteUserDetails loadUserDetails(String login);
    CompleteUserDetails loadUserDetailsByEmail(String email);
    void changeUserPassword(long userId, String hashedPassword);
}
