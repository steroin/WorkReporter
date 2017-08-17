package pl.workreporter.web.beans.entities.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
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
    public Solution loadSolution(long id) {
        String query = "select id, name, creation_date, last_edition_date from solution where id="+id;
        Solution solution = new Solution();
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        solution.setId(Integer.parseInt(result.get("id").toString()));
        solution.setName(result.get("name").toString());
        solution.setCreationDate(result.get("creation_date").toString());
        solution.setLastEditionDate(result.get("last_edition_date").toString());
        solution.setProjects(getSolutionProjects(id));
        solution.setTeams(getSolutionTeams(id));
        solution.setAdministrators(getSolutionAdministrators(id));
        solution.setEmployees(getSolutionEmployees(id));
        return solution;
    }

    @Override
    public List<Long> getSolutionProjects(long id) {
        List<Long> projects = new ArrayList<>();
        String query = "select id from project where solutionid="+id;

        List<Map<String, Object>> projectsResult = jdbcTemplate.queryForList(query);

        for (Map<String, Object> team : projectsResult) {
            projects.add(Long.parseLong(team.get("id").toString()));
        }
        return projects;
    }

    @Override
    public List<Long> getSolutionTeams(long id) {
        List<Long> teams = new ArrayList<>();
        String query = "select id from team where solutionid="+id;

        List<Map<String, Object>> teamsResult = jdbcTemplate.queryForList(query);

        for (Map<String, Object> team : teamsResult) {
            teams.add(Long.parseLong(team.get("id").toString()));
        }
        return teams;
    }

    @Override
    public List<Long> getSolutionAdministrators(long id) {
        List<Long> administrators = new ArrayList<>();
        String query = "select userid from solution_administrator where solutionid="+id;

        List<Map<String, Object>> administratorsResult = jdbcTemplate.queryForList(query);

        for (Map<String, Object> administrator : administratorsResult) {
            administrators.add(Long.parseLong(administrator.get("userid").toString()));
        }
        return administrators;
    }

    @Override
    public List<Long> getSolutionEmployees(long id) {
        List<Long> users = new ArrayList<>();
        String query = "select au.id "+
            "from solution s "+
            "inner join team t on s.id = t.solutionid "+
            "inner join appuser au on au.teamid = t.id "+
            "where s.id = "+id;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        for (Map<String, Object> user : result) {
            users.add(Long.parseLong(user.get("id").toString()));
        }
        return users;
    }

    @Override
    public Map<Long, String> getSolutionIdNameMap(long userId) {
        String query = "select s.id as solution_id, s.name as solution_name " +
                "from solution s " +
                "inner join solution_administrator sa on s.id = sa.solutionid " +
                "where sa.userid = "+userId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        Map<Long, String> map = new HashMap<>();

        for (Map<String, Object> row : result) {
            map.put(Long.parseLong(row.get("solution_id").toString()), row.get("solution_name").toString());
        }
        return map;
    }

    @Override
    @Transactional
    public void updateSolution(Solution solution) {
        String dateFormat = "YYYY-MM-DD HH24:MI:SS.FF";
        String creationDate = solution.getCreationDate();
        String lastEditionDate = solution.getLastEditionDate();
        String query = "update solution set name = ?, creation_date = to_timestamp(?, ?), last_edition_date = to_timestamp(?, ?) where id = ?";
        int ret = jdbcTemplate.update(query, solution.getName(), creationDate, dateFormat, lastEditionDate, dateFormat,  solution.getId());
        System.out.println(ret);
    }
}
