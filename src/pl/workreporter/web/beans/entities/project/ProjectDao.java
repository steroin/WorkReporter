package pl.workreporter.web.beans.entities.project;

import java.util.List;

/**
 * Created by Sergiusz on 15.08.2017.
 */
public interface ProjectDao {
    Project getProjectById(long id);
    List<Project> getAllProjectsInSolution(long solutionId);
    void removeProject(long solutionId, long projectId);
    void updateProject(Project project);
}
