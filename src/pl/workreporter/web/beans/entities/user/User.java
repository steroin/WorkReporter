package pl.workreporter.web.beans.entities.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import pl.workreporter.web.beans.entities.account.Account;
import pl.workreporter.web.beans.entities.personaldata.PersonalData;
import pl.workreporter.web.beans.entities.position.Position;
import pl.workreporter.web.beans.entities.solution.Solution;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sergiusz on 19.08.2017.
 */
@Entity(name = "User")
@Table(name = "APPUSER")
public class User implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appuserseq")
    @SequenceGenerator(name = "appuserseq", sequenceName = "appuserseq", allocationSize = 1)
    @JsonView(JsonDataView.User.class)
    private long id;
    @ManyToOne
    @JoinColumn(name = "SOLUTIONID")
    @JsonBackReference(value = "solutionEmployees")
    @JsonView(JsonDataView.User.class)
    private Solution solution;
    @ManyToOne
    @JoinColumn(name = "TEAMID")
    @JsonView(JsonDataView.Myself.class)
    private Team team;
    @OneToOne
    @JoinColumn(name = "ACCOUNTID")
    @JsonView(JsonDataView.Myself.class)
    private Account account;
    @ManyToOne
    @JoinColumn(name = "POSITIONID")
    @JsonView(JsonDataView.Myself.class)
    private Position position;
    @OneToOne
    @JoinColumn(name = "PERSONALDATAID")
    @JsonView(JsonDataView.User.class)
    private PersonalData personalData;
    @Column(name = "WORKING_TIME")
    @JsonView(JsonDataView.Myself.class)
    private double workingTime;
    @Column(name = "IS_SOLUTION_MANAGER")
    @JsonView(JsonDataView.Myself.class)
    private boolean isSolutionManager;
    @Column(name = "CREATION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+1")
    @JsonView(JsonDataView.SolutionManager.class)
    private Date creationDate = new Date();
    @Column(name = "LAST_EDITION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+1")
    @JsonView(JsonDataView.SolutionManager.class)
    private Date lastEditionDate = new Date();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public double getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(double workingTime) {
        this.workingTime = workingTime;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastEditionDate() {
        return lastEditionDate;
    }

    public void setLastEditionDate(Date lastEditionDate) {
        this.lastEditionDate = lastEditionDate;
    }

    public boolean isSolutionManager() {
        return isSolutionManager;
    }

    public void setSolutionManager(boolean solutionManager) {
        isSolutionManager = solutionManager;
    }
}
