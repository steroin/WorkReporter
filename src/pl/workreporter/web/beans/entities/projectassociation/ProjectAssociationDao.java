package pl.workreporter.web.beans.entities.projectassociation;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 22.08.2017.
 */
public interface ProjectAssociationDao {
    Map<Long, String> getProjectsTeams(long projectId);
    Map<Long, String> getTeamsProjects(long teamId);
}
