package pl.workreporter.web.beans.entities.project;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 15.08.2017.
 */
public interface ProjectDao {
    Project getProjectById(long id);
    List<Project> getAllProjectsInSolution(long solutionId);
    List<Project> getAllUsersProject(long userId);
    List<Project> getProjects(List<Long> projectIds);
    Project addProject(long solutionId, String name, String desc);
    void removeProject(long projectId);
    void removeProjects(List<Long> projectIds);
    Project updateProject(long projectId, Map<String, String> map);
}
