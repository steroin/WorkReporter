package pl.workreporter.web.beans.entities.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.entities.solution.Solution;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Override
    public Project getProjectById(long id) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        return entityManager.find(Project.class, id);
    }

    @Override
    public List<Project> getAllProjectsInSolution(long solutionId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("solution"), entityManager.find(Solution.class, solutionId)));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Project> getAllUsersProject(long userId) {
       String query = "select p.id from project p " +
                "join project_association pa on p.id=pa.projectid " +
                "join appuser au on au.teamid=pa.teamid " +
                "where au.id = "+userId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        List<Project> projects = new ArrayList<>();
        for (Map<String, Object> map : result) {
            projects.add(entityManager.find(Project.class, Long.parseLong(map.get("id").toString())));
        }
        return projects;
    }

    @Override
    public List<Project> getProjects(List<Long> projectIds) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        query.select(root);
        query.where(root.get("id").in(projectIds));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Project addProject(long solutionId, String name, String desc) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        Project project = new Project();
        project.setSolution(entityManager.find(Solution.class, solutionId));
        project.setName(name);
        project.setDescription(desc);
        entityManager.getTransaction().begin();
        entityManager.persist(project);
        entityManager.getTransaction().commit();
        return project;
    }

    @Override
    public void removeProject(long projectId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        Project project = entityManager.find(Project.class, projectId);
        entityManager.remove(project);
        entityManager.getTransaction().commit();
    }

    @Override
    public void removeProjects(List<Long> projectIds) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        for (long id : projectIds) {
            Project project = entityManager.find(Project.class, id);
            entityManager.remove(project);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public Project updateProject(long projectId, Map<String, String> map) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        Project project = entityManager.find(Project.class, projectId);
        project.setName(map.get("name"));
        project.setDescription(map.get("description"));
        entityManager.getTransaction().begin();
        entityManager.merge(project);
        entityManager.getTransaction().commit();
        return project;
    }
}
