package pl.workreporter.web.beans.entities.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.entities.user.UserDao;
import pl.workreporter.web.beans.security.rest.*;
import pl.workreporter.web.service.security.PrincipalAuthenticator;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 21.09.2017.
 */
@Repository
public class ProjectDaoWrapper {
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PrincipalAuthenticator authenticator;

    public RestResponse<Project> getProjectById(long id) {
        Project project;

        try {
            project = projectDao.getProjectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        if (authenticator.authenticateSolutionId(project.getSolution().getId())) {
            return new RestResponseSuccess<>(project);
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<List<Project>> getAllProjectsInSolution(long solutionId) {
        List<Project> projects;
        try {
            projects = projectDao.getAllProjectsInSolution(solutionId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        if (authenticator.authenticateSolutionId(solutionId)) {
            return new RestResponseSuccess<>(projects);
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<List<Project>> getAllUsersProject(long userId) {
        User user;

        try {
            user = userDao.getUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }

        List<Project> projects;

        if (!authenticator.authenticateSolutionAdministrator(user.getSolution().getId()) && !authenticator.authenticateUserId(userId)) {
            return new RestResponseAuthenticationError<>();
        }
        try {
            projects = projectDao.getAllUsersProject(userId);
            return new RestResponseSuccess<>(projects);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<Project> addProject(long solutionId, String name, String desc) {
        if (authenticator.authenticateSolutionAdministrator(solutionId)) {
            try {
                Project project = projectDao.addProject(solutionId, name, desc);
                return new RestResponseSuccess<>(project);
            } catch (Exception e) {
                return new RestResponseAdditionFailedError<>();
            }
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<Void> removeProject(long projectId) {
        Project project;

        try {
            project = projectDao.getProjectById(projectId);
        } catch (Exception e) {
            return new RestResponseRemovingFailedError<>();
        }
        if (authenticator.authenticateSolutionAdministrator(project.getSolution().getId())) {
            try {
                projectDao.removeProject(projectId);
                return new RestResponseSuccess<>();
            } catch (Exception e) {
                return new RestResponseRemovingFailedError<>();
            }
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<Void> removeProjects(List<Long> projectIds) {
        List<Project> projects;

        try {
            projects = projectDao.getProjects(projectIds);
        } catch (Exception e) {
            return new RestResponseRemovingFailedError<>();
        }

        for (Project project : projects) {
            if (!authenticator.authenticateSolutionAdministrator(project.getSolution().getId())) {
                return new RestResponseAuthenticationError<>();
            }
        }

        try {
            projectDao.removeProjects(projectIds);
            return new RestResponseSuccess<>();
        } catch (Exception e) {
            return new RestResponseRemovingFailedError<>();
        }
    }

    public RestResponse<Project> updateProject(long projectId, Map<String, String> map) {
        Project project;

        try {
            project = projectDao.getProjectById(projectId);
        } catch (Exception e) {
            return new RestResponseUpdateFailedError<>();
        }
        if (authenticator.authenticateSolutionAdministrator(project.getSolution().getId())) {
            try {
                return new RestResponseSuccess<>(projectDao.updateProject(projectId, map));
            } catch (Exception e) {
                return new RestResponseUpdateFailedError<>();
            }
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }
}
