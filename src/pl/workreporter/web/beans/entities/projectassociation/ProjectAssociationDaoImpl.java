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
    public Map<Long, String> getProjectsTeams(long projectId) {
        String query = "select * from project_association pa " +
                "join team t on pa.teamid = t.id " +
                "where pa.projectid = "+projectId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        Map<Long, String> teams = new HashMap<>();

        for (Map<String, Object> map : result) {
            teams.put(Long.parseLong(map.get("teamid").toString()), map.get("name").toString());
        }
        return teams;
    }

    @Override
    public Map<Long, String> getTeamsProjects(long teamId) {
        String query = "select * from project_association pa " +
                "join project p on pa.projectid = p.id " +
                "where pa.teamid = "+teamId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        Map<Long, String> projects = new HashMap<>();

        for (Map<String, Object> map : result) {
            projects.put(Long.parseLong(map.get("projectid").toString()), map.get("name").toString());
        }
        return projects;
    }
}
