package pl.workreporter.web.beans.entities.user;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 19.08.2017.
 */
public interface UserDao {
    User getUserById(long id);
    List<User> getAllUsersInSolution(long solutionId);
    List<User> getAllUsersInTeam(long teamId);
    User addUser(long solutionId, Long teamId, Long positionId, double workingTime, String firstName, String lastName,
                 String birthday, String phone, String login, String password, String email);
    void removeUser(long solutionId, long userId);
    void removeUsers(long solutionId, List<Long> userIds);
    User updateUser(long userId, Map<String, String> map);
}
