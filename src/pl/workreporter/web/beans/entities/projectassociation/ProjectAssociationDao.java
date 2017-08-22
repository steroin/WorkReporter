package pl.workreporter.web.beans.entities.projectassociation;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 22.08.2017.
 */
public interface ProjectAssociationDao {
    List<Map<String, String>> getProjectsTeams(long projectId);
    List<Map<String, String>> getTeamsProjects(long teamId);
    void updateTeamsProjectsState(long teamId, List<Long> projectsToAdd, List<Long> projectsToRemove);
    void updateProjectsTeamsState(long projectId, List<Long> teamsToAdd, List<Long> teamsToRemove);
}
