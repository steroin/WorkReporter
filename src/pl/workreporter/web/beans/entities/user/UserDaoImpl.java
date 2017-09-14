package pl.workreporter.web.beans.entities.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.entities.account.Account;
import pl.workreporter.web.beans.entities.logentry.LogEntry;
import pl.workreporter.web.beans.entities.personaldata.PersonalData;
import pl.workreporter.web.beans.entities.position.Position;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.solution.Solution;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.service.date.DateParser;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Created by Sergiusz on 19.08.2017.
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DateParser dateParser;
    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Override
    public User getUserById(long id) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> getAllUsersInSolution(long solutionId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("solution"), entityManager.find(Solution.class, solutionId)));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<User> getAllUsersInTeam(long teamId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("team"), entityManager.find(Team.class, teamId)));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public User addUser(long solutionId, Long teamId, long positionId, double workingTime, String firstName,
                        String lastName, String birthday, String phone, String login, String password, String email) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(16));

        Account account = new Account();
        account.setEmail(email);
        account.setHashedPassword(hashedPassword);
        account.setLogin(login);
        account.setStatus(1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        //do wyjebania przy implementacji error handlingu
        Date date = null;
        try {
            if (birthday != null && !birthday.isEmpty())
                date = sdf.parse(dateParser.makeParsableDate(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PersonalData personalData = new PersonalData();
        personalData.setBirthday(date);
        personalData.setFirstName(firstName);
        personalData.setLastName(lastName);
        personalData.setPhone(phone);

        User user = new User();
        user.setWorkingTime(workingTime);
        user.setAccount(account);
        user.setPersonalData(personalData);
        user.setPosition(entityManager.find(Position.class, positionId));
        user.setSolution(entityManager.find(Solution.class, solutionId));
        user.setTeam(teamId == null ? null : entityManager.find(Team.class, teamId));

        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.persist(personalData);
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        return user;
    }

    @Override
    public void removeUser(long solutionId, long userId) {
        //sprawdzic czy usuwamy solution admina
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        User user = entityManager.find(User.class, userId);
        long accountId = user.getAccount().getId();
        long personalDataId = user.getPersonalData().getId();
        entityManager.remove(user);
        Account account = entityManager.find(Account.class, accountId);
        entityManager.remove(account);
        PersonalData personalData = entityManager.find(PersonalData.class, personalDataId);
        entityManager.remove(personalData);
        entityManager.getTransaction().commit();
    }

    @Override
    public void removeUsers(long solutionId, List<Long> userIds) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        for (long id : userIds) {
            User user = entityManager.find(User.class, id);
            long accountId = user.getAccount().getId();
            long personalDataId = user.getPersonalData().getId();
            entityManager.remove(user);
            Account account = entityManager.find(Account.class, accountId);
            entityManager.remove(account);
            PersonalData personalData = entityManager.find(PersonalData.class, personalDataId);
            entityManager.remove(personalData);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public User updateUser(long userId, Map<String, String> map) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        User user = entityManager.find(User.class, userId);
        PersonalData personalData = user.getPersonalData();
        Account account = user.getAccount();

        entityManager.getTransaction().begin();

        if (map.containsKey("teamid")) {
            Team oldTeam = user.getTeam();
            Team newTeam = map.get("teamid") == null || map.get("teamid").isEmpty() ? null : entityManager.find(Team.class, Long.parseLong(map.get("teamid")));
            if (oldTeam != null && (newTeam == null || newTeam.getId() != oldTeam.getId())) {
                oldTeam.setSolution(user.getSolution());
                oldTeam.setLeaderId(null);
                oldTeam.setLeaderName("");
                entityManager.merge(oldTeam);
            }
            user.setTeam(newTeam);
        }
        if (map.containsKey("positionid")) {
            user.setPosition(map.get("positionid") == null || map.get("positionid").isEmpty() ? null : entityManager.find(Position.class, Long.parseLong(map.get("positionid"))));
        }
        if (map.containsKey("workingtime")) {
            user.setWorkingTime(Double.parseDouble(map.get("workingtime")));
        }
        if (map.containsKey("firstname")) {
            personalData.setFirstName(map.get("firstname"));
        }
        if (map.containsKey("lastname")) {
            personalData.setLastName(map.get("lastname"));
        }

        if (map.containsKey("birthday")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            if (map.get("birthday") == null || map.get("birthday").isEmpty()) user.getPersonalData().setBirthday(null);
            else {
                //do wyjebania przy implementacji error handlingu
                Date date = null;
                try {
                    date = sdf.parse(dateParser.makeParsableDate(map.get("birthday")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                user.getPersonalData().setBirthday(date);
            }
        }
        if (map.containsKey("phone")) {
            personalData.setPhone(map.get("phone") == null || map.get("phone").isEmpty() ? null : map.get("phone"));
        }
        if (map.containsKey("authentication")) {
            account.setLogin(map.get("authentication"));
        }
        if (map.containsKey("email")) {
            account.setEmail(map.get("email"));
        }

        entityManager.merge(account);
        entityManager.merge(personalData);
        entityManager.merge(user);
        entityManager.getTransaction().commit();
        return user;
    }
}
