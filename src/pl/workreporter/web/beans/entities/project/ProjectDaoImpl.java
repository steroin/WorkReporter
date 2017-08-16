package pl.workreporter.web.beans.entities.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        SimpleDateFormat srcSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        SimpleDateFormat destSdf = new SimpleDateFormat("dd.MM.yyyy");
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        Project project = new Project();
        project.setId(Long.parseLong(result.get("id").toString()));
        project.setSolutionId(Long.parseLong(result.get("solutionid").toString()));
        project.setName(result.get("name").toString());
        project.setDescription(result.get("description").toString());
        try {
            project.setCreationDate(destSdf.format(srcSdf.parse(result.get("creation_date").toString())));
            project.setLastEditionDate(destSdf.format(srcSdf.parse(result.get("last_edition_date").toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
            project.setCreationDate(map.get("creation_date").toString());
            project.setLastEditionDate(map.get("last_edition_date").toString());
            projects.add(project);
        }
        return projects;
    }

    @Override
    public void removeProject(long solutionId, long projectId) {
        String query = "delete from project where id="+projectId+" and solutionid="+solutionId;
        jdbcTemplate.execute(query);
    }
}
