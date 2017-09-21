package pl.workreporter.web.beans.entities.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 12.08.2017.
 */

@Repository
public class SolutionDaoImpl implements SolutionDao {

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Override
    public Solution loadSolution(long id) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        return entityManager.find(Solution.class, id);
    }

    @Override
    public Solution updateSolution(Solution solution) {
        solution.setLastEditionDate(new Date());
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(solution);
        entityManager.getTransaction().commit();
        return entityManager.find(Solution.class, solution.getId());
    }
}
