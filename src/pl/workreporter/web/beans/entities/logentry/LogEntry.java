package pl.workreporter.web.beans.entities.logentry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sergiusz on 21.08.2017.
 */
@Entity
@Table(name = "LOG_ENTRY")
@SecondaryTable(name = "LOG_TYPE")
public class LogEntry implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logentryseq")
    @SequenceGenerator(name = "logentryseq", sequenceName = "logentryseq", allocationSize = 1)
    @JsonView(JsonDataView.User.class)
    private long id;
    @ManyToOne
    @JoinColumn(name = "USERID")
    @JsonView(JsonDataView.User.class)
    private User user;
    @ManyToOne
    @JoinColumn(name = "TEAMID")
    @JsonView(JsonDataView.User.class)
    private Team team;
    @ManyToOne
    @JoinColumn(name = "LOGTYPEID")
    @JsonView(JsonDataView.User.class)
    private LogType logType;
    @ManyToOne
    @JoinColumn(name = "PROJECTID")
    @JsonView(JsonDataView.User.class)
    private Project project;
    @Column(name = "LOG_START")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+1")
    @JsonView(JsonDataView.User.class)
    private Date logStart;
    @Column(name = "LOGGEDHOURS")
    @JsonView(JsonDataView.User.class)
    private double loggedHours;
    @Column(name = "LOG_DATE")
    @JsonView(JsonDataView.User.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+1")
    private Date logDate = new Date();
    @Column(name = "LAST_EDITION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+1")
    @JsonView(JsonDataView.User.class)
    private Date lastEditionDate = new Date();
    @Column(name = "STATUS")
    @JsonView(JsonDataView.User.class)
    private int status = 1;
    @ManyToOne
    @JoinColumn(name = "ACCEPTEDBY")
    @JsonView(JsonDataView.User.class)
    private User acceptedBy = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getLogStart() {
        return logStart;
    }

    public void setLogStart(Date logStart) {
        this.logStart = logStart;
    }

    public double getLoggedHours() {
        return loggedHours;
    }

    public void setLoggedHours(double loggedHours) {
        this.loggedHours = loggedHours;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public Date getLastEditionDate() {
        return lastEditionDate;
    }

    public void setLastEditionDate(Date lastEditionDate) {
        this.lastEditionDate = lastEditionDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(User acceptedBy) {
        this.acceptedBy = acceptedBy;
    }
}
