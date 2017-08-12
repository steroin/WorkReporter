package pl.workreporter.web.beans.entities.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 12.08.2017.
 */

@Repository
public class SolutionDaoImpl implements SolutionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Solution loadSolution(int id) {
        String query = "select id, name, creation_date, last_edition_date from solution where id="+id;

        Solution solution = new Solution();
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        solution.setId(Integer.parseInt(result.get("id").toString()));
        solution.setName(result.get("name").toString());
        solution.setCreationDate(Date.valueOf(result.get("creation_date").toString()));
        solution.setLastEditionDate(Date.valueOf(result.get("last_edition_date").toString()));
        solution.setProjects(getSolutionProjects(id));
        solution.setTeams(getSolutionTeams(id));
        solution.setAdministrators(getSolutionAdministrators(id));
        return solution;
    }

    @Override
    public List<Integer> getSolutionProjects(int id) {
        List<Integer> projects = new ArrayList<>();
        String query = "select id from project where solutionid="+id;

        List<Map<String, Object>> projectsResult = jdbcTemplate.queryForList(query);

        for (Map<String, Object> team : projectsResult) {
            projects.add(Integer.parseInt(team.get("id").toString()));
        }
        return projects;
    }

    @Override
    public List<Integer> getSolutionTeams(int id) {
        List<Integer> teams = new ArrayList<>();
        String query = "select id from team where solutionid="+id;

        List<Map<String, Object>> teamsResult = jdbcTemplate.queryForList(query);

        for (Map<String, Object> team : teamsResult) {
            teams.add(Integer.parseInt(team.get("id").toString()));
        }
        return teams;
    }

    @Override
    public List<Integer> getSolutionAdministrators(int id) {
        List<Integer> administrators = new ArrayList<>();
        String query = "select userid from solution_administrator where solutionid="+id;

        List<Map<String, Object>> administratorsResult = jdbcTemplate.queryForList(query);

        for (Map<String, Object> administrator : administratorsResult) {
            administrators.add(Integer.parseInt(administrator.get("id").toString()));
        }
        return administrators;
    }
}