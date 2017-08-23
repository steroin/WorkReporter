package pl.workreporter.web.beans.entities.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.service.date.DateParser;

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
    @Autowired
    private DateParser dateParser;

    @Override
    public Project getProjectById(long id) {
        String query = "select * from project where id="+id;
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        Project project = new Project();
        project.setId(Long.parseLong(result.get("id").toString()));
        project.setSolutionId(Long.parseLong(result.get("solutionid").toString()));
        project.setName(result.get("name").toString());
        project.setDescription(result.get("description") == null ? "" : result.get("description").toString());
        project.setCreationDate(dateParser.parseToReadableDate(result.get("creation_date").toString()));
        project.setLastEditionDate(dateParser.parseToReadableDate(result.get("last_edition_date").toString()));
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
            project.setDescription(map.get("description") == null ? "" : map.get("description").toString());
            project.setCreationDate(dateParser.parseToReadableDate(map.get("creation_date").toString()));
            project.setLastEditionDate(dateParser.parseToReadableDate(map.get("last_edition_date").toString()));
            projects.add(project);
        }
        return projects;
    }

    @Override
    public List<Project> getAllUsersProject(long userId) {
        String query = "select p.id, p.solutionid, p.name, p.description, p.creation_date, p.last_edition_date from project p " +
                "join project_association pa on p.id=pa.projectid " +
                "join appuser au on au.teamid=pa.teamid " +
                "where au.id = "+userId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<Project> projects = new ArrayList<>();

        for (Map<String, Object> map : result) {
            Project project = new Project();
            project.setId(Long.parseLong(map.get("id").toString()));
            project.setSolutionId(Long.parseLong(map.get("solutionid").toString()));
            project.setName(map.get("name").toString());
            project.setDescription(map.get("description") == null ? "" : map.get("description").toString());
            project.setCreationDate(dateParser.parseToReadableDate(map.get("creation_date").toString()));
            project.setLastEditionDate(dateParser.parseToReadableDate(map.get("last_edition_date").toString()));
            projects.add(project);
        }
        return projects;
    }

    @Override
    public Project addProject(long solutionId, String name, String desc) {
        String query = "select projectseq.nextval from dual";
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        long id = Long.parseLong(result.get("nextval").toString());

        query = "insert into project(id, solutionid, name, description, creation_date, last_edition_date) values (?, ?, ?, ?, sysdate, sysdate)";
        jdbcTemplate.update(query, id, solutionId, name, desc);
        return getProjectById(id);
    }

    @Override
    public void removeProject(long solutionId, long projectId) {
        String query = "delete from project where id="+projectId+" and solutionid="+solutionId;
        jdbcTemplate.execute(query);
    }

    @Override
    public void removeProjects(long solutionId, List<Long> projectIds) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from project where solutionid="+solutionId);
        queryBuilder.append(" and (");
        for (int i = 0; i < projectIds.size(); i++) {
            queryBuilder.append("id="+projectIds.get(i));
            if (i < projectIds.size() - 1) {
                queryBuilder.append(" or ");
            }
        }
        queryBuilder.append(")");
        jdbcTemplate.execute(queryBuilder.toString());
    }

    @Override
    public void updateProject(Project project) {
        String query = "update project " +
                "set solutionid = "+project.getSolutionId()+", " +
                "name = '"+project.getName()+"', " +
                "description = '"+project.getDescription()+"', " +
                "creation_date = "+dateParser.parseToDatabaseTimestamp(project.getCreationDate())+", " +
                "last_edition_date = sysdate " +
                "where id = "+project.getId();
        jdbcTemplate.execute(query);
    }
}
