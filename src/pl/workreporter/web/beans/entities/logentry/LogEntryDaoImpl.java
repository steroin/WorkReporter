package pl.workreporter.web.beans.entities.logentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.service.date.DateParser;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class LogEntryDaoImpl implements LogEntryDao {

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;
    @Autowired
    private DateParser dateParser;

    @Override
    public LogEntry getLogEntryById(long id) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        return entityManager.find(LogEntry.class, id);
    }

    @Override
    public LogEntry addLogEntry(long userId, String startDate, double loggedHours,
                                long logTypeId, Long projectId) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        //do wyjebania przy implementacji error handlingu
        Date date = null;
        try {
            date = sdf.parse(dateParser.makeParsableDate(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();

        User user = entityManager.find(User.class, userId);
        LogEntry logEntry = new LogEntry();
        logEntry.setUser(user);
        logEntry.setLogStart(date);
        logEntry.setLoggedHours(loggedHours);
        logEntry.setLogType(entityManager.find(LogType.class, logTypeId));
        logEntry.setProject(projectId == null ? null : entityManager.find(Project.class, projectId));
        logEntry.setTeam(user.getTeam());
        entityManager.getTransaction().begin();
        entityManager.persist(logEntry);
        entityManager.getTransaction().commit();
        return logEntry;
    }

    @Override
    public List<LogEntry> getDailyLogEntries(long userId, int year, int month, int day) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LogEntry> query = criteriaBuilder.createQuery(LogEntry.class);
        Root<LogEntry> root = query.from(LogEntry.class);
        query.select(root);
        Path<Date> start = root.get("logStart");
        query.where(criteriaBuilder.and(
                criteriaBuilder.equal(criteriaBuilder.function("year", Integer.class, start), year),
                criteriaBuilder.equal(criteriaBuilder.function("month", Integer.class, start), month),
                criteriaBuilder.equal(criteriaBuilder.function("day", Integer.class, start), day),
                criteriaBuilder.equal(root.get("user"), entityManager.find(User.class, userId))));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<LogEntry> getLastLogEntries(long userId, int period) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -period);
        date = calendar.getTime();

        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LogEntry> query = criteriaBuilder.createQuery(LogEntry.class);
        Root<LogEntry> root = query.from(LogEntry.class);
        query.select(root);
        query.where(criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(root.get("logStart"), date),
                criteriaBuilder.equal(root.get("user"), entityManager.find(User.class, userId))));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<LogType> getAllLogTypes() {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LogType> query = criteriaBuilder.createQuery(LogType.class);
        Root<LogType> root = query.from(LogType.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void changeLogEntryStatus(long logEntryId, int status) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        LogEntry logEntry = entityManager.find(LogEntry.class, logEntryId);
        logEntry.setStatus(status);
        entityManager.getTransaction().begin();
        entityManager.merge(logEntry);
        entityManager.getTransaction().commit();
    }

    @Override
    public void updateLogEntry(LogEntry logEntry) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(logEntry);
        entityManager.getTransaction().commit();
    }

    @Override
    public void removeLogEntry(long logEntryId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        LogEntry logEntry = entityManager.find(LogEntry.class, logEntryId);
        entityManager.getTransaction().begin();
        entityManager.remove(logEntry);
        entityManager.getTransaction().commit();
    }

    @Override
    public void removeLogEntries(List<Long> entries) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        for (long id : entries) {
            LogEntry logEntry = entityManager.find(LogEntry.class, id);
            entityManager.remove(logEntry);
        }
        entityManager.getTransaction().commit();
    }
}
