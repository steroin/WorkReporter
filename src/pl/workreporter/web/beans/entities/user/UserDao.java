package pl.workreporter.web.beans.entities.user;

import java.util.List;

/**
 * Created by Sergiusz on 19.08.2017.
 */
public interface UserDao {
    User getUserById(long id);
    List<User> getAllUsersInSolution(long solutionId);
    List<User> getAllUsersInTeam(long teamId);
    User addUser(long solutionId, Long teamId, long positionId, double workingTime, String firstName, String lastName,
                 String birthday, String phone, String login, String password, String email);
    void removeUser(long solutionId, long userId, long personalDataId, long accountId);
    void removeUsers(long solutionId, List<Long> userIds, List<Long> personalDataIds, List<Long> accountIds);
    void updateUser(User user);
    void changePassword(long id, String newPassword);
}
