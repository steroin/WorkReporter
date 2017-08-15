package pl.workreporter.web.beans.entities.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 15.08.2017.
 */

@Repository
public class ProjectDaoImpl implements ProjectDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Project getProjectById(long id) {
        String query = "select * from project where id="+id;
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        Project project = new Project();
        project.setId(Long.parseLong(result.get("id").toString()));
        project.setSolutionId(Long.parseLong(result.get("solutionid").toString()));
        project.setName(result.get("name").toString());
        project.setDescription(result.get("description").toString());
        return project;
    }

    @Override
    public List<Project> getAllProjectsInSolution(long solutionId) {
        String query = "select * from project where solutionid="+solutionId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<Project> projects = new ArrayList<>();

        for (Map<String, Object> map : result) {
            Project project = new Project();
            project.setId(Long.parseLong(map.get("id").toString()));
            project.setSolutionId(Long.parseLong(map.get("solutionid").toString()));
            project.setName(map.get("name").toString());
            project.setDescription(map.get("description").toString());
            projects.add(project);
        }
        return projects;
    }
}
