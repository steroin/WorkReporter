package pl.workreporter.web.beans.entities.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.entities.user.UserDao;
import pl.workreporter.web.beans.security.rest.*;
import pl.workreporter.web.service.security.PrincipalAuthenticator;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 22.09.2017.
 */
@Repository
public class TeamDaoWrapper {
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PrincipalAuthenticator authenticator;

    public RestResponse<Team> getTeamById(long id) {
        Team team;

        try {
            team = teamDao.getTeamById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        if (authenticator.authenticateSolutionAdministrator(team.getSolution().getId()) ||
                authenticator.authenticateTeamId(team.getId())) {
            return new RestResponseSuccess<>(team);
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<List<Team>> getAllTeamsInSolution(long solutionId) {
        try {
            if (authenticator.authenticateSolutionAdministrator(solutionId)) {
                return new RestResponseSuccess<>(teamDao.getAllTeamsInSolution(solutionId));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<List<Team>> getAllTeamsManagedBy(long userId) {
        User user;

        try {
            user = userDao.getUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
        try {
            if (authenticator.authenticateSolutionAdministrator(user.getSolution().getId()) ||
                    authenticator.authenticateUserId(user.getId())) {
                return new RestResponseSuccess<>(teamDao.getAllTeamsManagedBy(userId));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<Team> addTeam(long solutionId, String name, Long leaderId) {
        try {
            if (leaderId != null) {
                User user = userDao.getUserById(leaderId);
                if (user.getSolution().getId() != solutionId) {
                    return new RestResponseAdditionFailedError<>();
                }
            }
            if (authenticator.authenticateSolutionAdministrator(solutionId)) {
                return new RestResponseSuccess<>(teamDao.addTeam(solutionId, name, leaderId));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseAdditionFailedError<>();
        }
    }

    public RestResponse<Void> removeTeam(long teamId) {
        try {
            Team team = teamDao.getTeamById(teamId);
            if (authenticator.authenticateSolutionAdministrator(team.getSolution().getId())) {
                teamDao.removeTeam(teamId);
                return new RestResponseSuccess<>();
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseRemovingFailedError<>();
        }
    }

    public RestResponse<Void> removeTeams(List<Long> teamIds) {

        List<Team> teams;

        try {
            teams = teamDao.getTeams(teamIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        boolean authenticated = true;

        for (Team team : teams) {
            if (!authenticator.authenticateSolutionAdministrator(team.getSolution().getId())) {
                authenticated = false;
            }
        }
        if (!authenticated) {
            return new RestResponseAuthenticationError<>();
        }
        try {
            teamDao.removeTeams(teamIds);
            return new RestResponseSuccess<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<Team> updateTeam(long teamId, Map<String, String> map) {
        try {
            Team team = teamDao.getTeamById(teamId);
            if (authenticator.authenticateSolutionAdministrator(team.getSolution().getId())) {
                return new RestResponseSuccess<>(teamDao.updateTeam(teamId, map));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            return new RestResponseUpdateFailedError<>();
        }
    }
}
