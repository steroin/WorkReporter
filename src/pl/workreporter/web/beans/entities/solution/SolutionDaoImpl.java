package pl.workreporter.web.beans.entities.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Override
    public Solution loadSolution(long id) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        return entityManager.find(Solution.class, id);
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
    public void updateSolution(Solution solution) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(solution);
        entityManager.getTransaction().commit();
    }
}
