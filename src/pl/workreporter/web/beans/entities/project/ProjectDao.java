package pl.workreporter.web.beans.entities.project;

import java.util.List;

/**
 * Created by Sergiusz on 15.08.2017.
 */
public interface ProjectDao {
    Project getProjectById(long id);
    List<Project> getAllProjectsInSolution(long solutionId);
    List<Project> getAllUsersProject(long userId);
    Project addProject(long solutionId, String name, String desc);
    void removeProject(long solutionId, long projectId);
    void removeProjects(long solutionId, List<Long> projectIds);
    void updateProject(Project project);
}
