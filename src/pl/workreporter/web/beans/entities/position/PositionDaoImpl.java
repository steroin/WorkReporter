package pl.workreporter.web.beans.entities.position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.entities.solution.Solution;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 19.08.2017.
 */
@Repository
public class PositionDaoImpl implements PositionDao {
    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Override
    public Position getPositionById(long id) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        return entityManager.find(Position.class, id);
    }

    @Override
    public List<Position> getAllPositionsInSolution(long solutionId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Position> query = criteriaBuilder.createQuery(Position.class);
        Root<Position> root = query.from(Position.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("solution"), entityManager.find(Solution.class, solutionId)));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Position> getPositions(List<Long> positionsIds) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Position> query = criteriaBuilder.createQuery(Position.class);
        Root<Position> root = query.from(Position.class);
        query.select(root);
        query.where(root.get("id").in(positionsIds));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Position addPosition(long solutionId, String name) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        Position position = new Position();
        position.setName(name);
        position.setSolution(entityManager.find(Solution.class, solutionId));
        entityManager.getTransaction().begin();
        entityManager.persist(position);
        entityManager.getTransaction().commit();
        return position;
    }

    @Override
    public void removePosition(long positionId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        Position position = entityManager.find(Position.class, positionId);
        entityManager.remove(position);
        entityManager.getTransaction().commit();
    }

    @Override
    public void removePositions(List<Long> positionsIds) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        for (long id : positionsIds) {
            Position position = entityManager.find(Position.class, id);
            entityManager.remove(position);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public Position updatePosition(long positionId, Map<String, String> map) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        Position position = entityManager.find(Position.class, positionId);
        position.setName(map.get("name"));
        entityManager.getTransaction().begin();
        entityManager.merge(position);
        entityManager.getTransaction().commit();
        return position;
    }
}
