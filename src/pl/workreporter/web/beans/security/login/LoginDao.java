package pl.workreporter.web.beans.security.login;

import pl.workreporter.security.login.CompleteUserDetails;

import java.util.List;

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
    CompleteUserDetails loadUserDetails(String login);
    CompleteUserDetails loadUserDetailsByEmail(String email);
    List<Integer> getManagedSolutions(String login);
    List<Integer> getManagedSolutionsByEmail(String email);
    List<Integer> getManagedTeams(String login);
    List<Integer> getManagedTeamsByEmail(String email);
}
