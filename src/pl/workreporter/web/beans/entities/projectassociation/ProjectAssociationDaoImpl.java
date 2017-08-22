package pl.workreporter.web.beans.entities.projectassociation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 22.08.2017.
 */
@Repository
public class ProjectAssociationDaoImpl implements ProjectAssociationDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, String>> getProjectsTeams(long projectId) {
        String query = "select pa.teamid as id, t.name as name from project_association pa " +
                "join team t on pa.teamid = t.id " +
                "where pa.projectid = "+projectId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<Map<String, String>> ret = new ArrayList<>();
        for (Map<String, Object> map : result) {
            Map<String, String> element = new HashMap<>();
            element.put("id", map.get("id").toString());
            element.put("name", map.get("name").toString());
            ret.add(element);
        }
        return ret;
    }

    @Override
    public List<Map<String, String>> getTeamsProjects(long teamId) {
        String query = "select pa.projectid as id, p.name as name from project_association pa " +
                "join project p on pa.projectid = p.id " +
                "where pa.teamid = "+teamId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<Map<String, String>> ret = new ArrayList<>();

        for (Map<String, Object> map : result) {
            Map<String, String> element = new HashMap<>();
            element.put("id", map.get("id").toString());
            element.put("name", map.get("name").toString());
            ret.add(element);
        }
        return ret;
    }

    @Override
    public void updateTeamsProjectsState(long teamId, List<Long> projectsToAdd, List<Long> projectsToRemove) {
        if (projectsToAdd.isEmpty() && projectsToRemove.isEmpty()) return;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("begin \n");
        if (!projectsToRemove.isEmpty()) {
            queryBuilder.append("delete from project_association where teamid = " + teamId + " and projectid in (");
            int i = 0;
            for (long id : projectsToRemove) {
                i++;
                queryBuilder.append(id + "");

                if (i < projectsToRemove.size()) {
                    queryBuilder.append(", ");
                }
            }
            queryBuilder.append("); \n");
        }

        if (!projectsToAdd.isEmpty()) {
            for (long id : projectsToAdd) {
                queryBuilder.append("insert into project_association(id, teamid, projectid) values (projectassociationseq.nextval, " + teamId + ", " + id + "); \n");
            }
        }
        queryBuilder.append("end;");
        jdbcTemplate.execute(queryBuilder.toString());
    }

    @Override
    public void updateProjectsTeamsState(long projectId, List<Long> teamsToAdd, List<Long> teamsToRemove) {
        if (teamsToAdd.isEmpty() && teamsToRemove.isEmpty()) return;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("begin \n");
        if (!teamsToRemove.isEmpty()) {
            queryBuilder.append("delete from project_association where projectid = " + projectId + " and teamid in (");
            int i = 0;
            for (long id : teamsToRemove) {
                i++;
                queryBuilder.append(id + "");

                if (i < teamsToRemove.size()) {
                    queryBuilder.append(", ");
                }
            }
            queryBuilder.append("); \n");
        }

        if (!teamsToAdd.isEmpty()) {
            for (long id : teamsToAdd) {
                queryBuilder.append("insert into project_association(id, teamid, projectid) values (projectassociationseq.nextval, " + id + ", " + projectId + "); \n");
            }
        }
        queryBuilder.append("end;");
        jdbcTemplate.execute(queryBuilder.toString());
    }
}
