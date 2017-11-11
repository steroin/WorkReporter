package pl.workreporter.web.beans.entities.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.workreporter.web.beans.entities.solution.Solution;
import pl.workreporter.web.beans.entities.user.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 19.08.2017.
 */
@Repository
@Transactional
public class TeamDaoImpl implements TeamDao{
    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Override
    public Team getTeamById(long id) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        return entityManager.find(Team.class, id);
    }

    @Override
    public List<Team> getAllTeamsInSolution(long solutionId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Team> query = criteriaBuilder.createQuery(Team.class);
        Root<Team> root = query.from(Team.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("solution"), entityManager.find(Solution.class, solutionId)));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Team> getAllTeamsManagedBy(long userId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Team> query = criteriaBuilder.createQuery(Team.class);
        Root<Team> root = query.from(Team.class);
        Join<Team, Solution> solutionJoin = root.join("solution");
        Join<Solution, User> userJoin = solutionJoin.join("employees", JoinType.LEFT);
        query.select(root).distinct(true);
        query.where(criteriaBuilder.or(
                criteriaBuilder.equal(root.get("leaderId"), userId),
                criteriaBuilder.and(
                        criteriaBuilder.equal(userJoin.get("isSolutionManager"), true),
                        criteriaBuilder.equal(userJoin.get("id"), userId)
                )));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Team> getTeams(List<Long> teamsIds) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Team> query = criteriaBuilder.createQuery(Team.class);
        Root<Team> root = query.from(Team.class);
        query.select(root);
        query.where(root.get("id").in(teamsIds));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Team addTeam(long solutionId, String name, Long leaderId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        Team team = new Team();
        team.setName(name);
        team.setSolution(entityManager.find(Solution.class, solutionId));
        team.setLeaderId(leaderId);

        entityManager.getTransaction().begin();
        entityManager.persist(team);
        entityManager.getTransaction().commit();
        return team;
    }

    @Override
    public void removeTeam(long teamId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        Team project = entityManager.find(Team.class, teamId);
        entityManager.remove(project);
        entityManager.getTransaction().commit();
    }

    @Override
    public void removeTeams(List<Long> teamIds) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        for (long id : teamIds) {
            Team project = entityManager.find(Team.class, id);
            entityManager.remove(project);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public Team updateTeam(long teamId, Map<String, String> map) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        Team team = entityManager.find(Team.class, teamId);
        team.setName(map.get("name"));
        if (map.get("leaderId") == null || map.get("leaderId").isEmpty()) {
            team.setLeaderId(null);
            team.setLeaderName("");
        } else {
            User leader = entityManager.find(User.class, Long.parseLong(map.get("leaderId")));
            team.setLeaderId(leader.getId());
            team.setLeaderName(leader.getPersonalData().getFirstName()+" "+leader.getPersonalData().getLastName());
        }
        entityManager.getTransaction().begin();
        entityManager.merge(team);
        entityManager.getTransaction().commit();
        return team;
    }
}
