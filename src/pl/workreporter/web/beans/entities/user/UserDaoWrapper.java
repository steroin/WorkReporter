package pl.workreporter.web.beans.entities.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.entities.position.Position;
import pl.workreporter.web.beans.entities.position.PositionDao;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.entities.team.TeamDao;
import pl.workreporter.web.beans.security.rest.*;
import pl.workreporter.web.service.security.PrincipalAuthenticator;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 22.09.2017.
 */
@Repository
public class UserDaoWrapper {

    @Autowired
    private UserDao userDao;
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private PositionDao positionDao;
    @Autowired
    private PrincipalAuthenticator authenticator;

    public RestResponse<User> getUserById(long id) {
        User user;

        try {
            user = userDao.getUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        if (authenticator.authenticateSolutionAdministrator(user.getSolution().getId()) ||
                authenticator.authenticateSolutionId(user.getSolution().getId())) {
            return new RestResponseSuccess<>(user);
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<List<User>> getAllUsersInSolution(long solutionId) {
        try {
            if (authenticator.authenticateSolutionAdministrator(solutionId)) {
                return new RestResponseSuccess<>(userDao.getAllUsersInSolution(solutionId));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<List<User>> getAllUsersInTeam(long teamId) {
        try {
            Team team = teamDao.getTeamById(teamId);

            if (authenticator.authenticateSolutionAdministrator(team.getSolution().getId()) ||
                authenticator.authenticateTeamAdministrator(team.getId())) {
                return new RestResponseSuccess<>(userDao.getAllUsersInTeam(teamId));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<User> addUser(long solutionId, Long teamId, Long positionId, double workingTime, String firstName,
                        String lastName, String birthday, String phone, String login, String password, String email) {
        try {
            if (teamId != null) {
                Team team = teamDao.getTeamById(teamId);
                if (team.getSolution().getId() != solutionId) {
                    return new RestResponseAdditionFailedError<>();
                }
            }
            if (positionId != null) {
                Position position = positionDao.getPositionById(positionId);
                if (position.getSolution().getId() != solutionId) {
                    return new RestResponseAdditionFailedError<>();
                }
            }

            if (authenticator.authenticateSolutionAdministrator(solutionId)) {
                return new RestResponseSuccess<>(userDao.addUser(solutionId, teamId, positionId, workingTime, firstName, lastName, birthday, phone, login, password, email));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseAdditionFailedError<>();
        }
    }

    public RestResponse<Void> removeUser(long userId) {
        try {
            User user = userDao.getUserById(userId);
            if (authenticator.authenticateSolutionAdministrator(user.getSolution().getId())) {
                userDao.removeUser(userId);
                return new RestResponseSuccess<>();
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseRemovingFailedError<>();
        }
    }

    public RestResponse<Void> removeUsers(List<Long> userIds) {
        List<User> users;

        try {
            users = userDao.getUsers(userIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        boolean authenticated = true;

        for (User user : users) {
            if (!authenticator.authenticateSolutionAdministrator(user.getSolution().getId())) {
                authenticated = false;
            }
        }
        if (!authenticated) {
            return new RestResponseAuthenticationError<>();
        }
        try {
            userDao.removeUsers(userIds);
            return new RestResponseSuccess<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<User> updateUser(long userId, Map<String, String> map) {
        try {
            User user = userDao.getUserById(userId);
            if (authenticator.authenticateSolutionAdministrator(user.getSolution().getId())) {
                return new RestResponseSuccess<>(userDao.updateUser(userId, map));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            return new RestResponseUpdateFailedError<>();
        }
    }
}
