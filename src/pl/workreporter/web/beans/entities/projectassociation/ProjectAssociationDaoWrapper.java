package pl.workreporter.web.beans.entities.projectassociation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.project.ProjectDao;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.entities.team.TeamDao;
import pl.workreporter.web.beans.security.rest.*;
import pl.workreporter.web.service.security.PrincipalAuthenticator;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 21.09.2017.
 */
@Repository
public class ProjectAssociationDaoWrapper {
    @Autowired
    private ProjectAssociationDao projectAssociationDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private PrincipalAuthenticator authenticator;

    public RestResponse<List<Map<String, String>>> getProjectsTeams(long projectId) {
        Project project;

        try {
            project = projectDao.getProjectById(projectId);
            if (authenticator.authenticateSolutionAdministrator(project.getSolution().getId())) {
                return new RestResponseSuccess<>(projectAssociationDao.getProjectsTeams(projectId));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<List<Map<String, String>>> getTeamsProjects(long teamId) {
        Team team;

        try {
            team = teamDao.getTeamById(teamId);
            if (authenticator.authenticateSolutionAdministrator(team.getSolution().getId())) {
                return new RestResponseSuccess<>(projectAssociationDao.getTeamsProjects(teamId));
            } else {
                return new RestResponseAuthenticationError<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<Void> updateTeamsProjectsState(long teamId, List<Long> projectsToAdd, List<Long> projectsToRemove) {
        Team team;

        try {
            team = teamDao.getTeamById(teamId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseUpdateFailedError<>();
        }

        if (authenticator.authenticateSolutionAdministrator(team.getSolution().getId())) {
            try {
                List<Project> projects = projectDao.getProjects(projectsToAdd);
                for (Project project : projects) {
                    if (!authenticator.authenticateSolutionAdministrator(project.getSolution().getId())) {
                        return new RestResponseAuthenticationError<>();
                    }
                }
                projectAssociationDao.updateTeamsProjectsState(teamId, projectsToAdd, projectsToRemove);
                return new RestResponseSuccess<>();
            } catch (Exception e) {
                e.printStackTrace();
                return new RestResponseUpdateFailedError<>();
            }
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<Void> updateProjectsTeamsState(long projectId, List<Long> teamsToAdd, List<Long> teamsToRemove) {
        Project project;

        try {
            project = projectDao.getProjectById(projectId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseUpdateFailedError<>();
        }

        if (authenticator.authenticateSolutionAdministrator(project.getSolution().getId())) {
            try {
                List<Team> teams = teamDao.getTeams(teamsToAdd);
                for (Team team : teams) {
                    if (!authenticator.authenticateSolutionAdministrator(team.getSolution().getId())) {
                        return new RestResponseAuthenticationError<>();
                    }
                }
                projectAssociationDao.updateProjectsTeamsState(projectId, teamsToAdd, teamsToRemove);
                return new RestResponseSuccess<>();
            } catch (Exception e) {
                e.printStackTrace();
                return new RestResponseUpdateFailedError<>();
            }
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }
}
