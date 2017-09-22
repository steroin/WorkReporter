package pl.workreporter.web.beans.entities.team;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Formula;
import pl.workreporter.web.beans.entities.solution.Solution;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sergiusz on 19.08.2017.
 */
@Entity
@Table(name = "TEAM")
public class Team implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teamseq")
    @SequenceGenerator(name = "teamseq", sequenceName = "teamseq", allocationSize = 1)
    @JsonView(JsonDataView.User.class)
    private long id;
    @ManyToOne
    @JoinColumn(name = "SOLUTIONID")
    @JsonBackReference(value = "solutionTeams")
    @JsonView(JsonDataView.User.class)
    private Solution solution;
    @Column(name = "NAME")
    @JsonView(JsonDataView.User.class)
    private String name;
    @Column(name = "CREATION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+2")
    @JsonView(JsonDataView.SolutionManager.class)
    private Date creationDate = new Date();
    @Column(name = "LAST_EDITION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+2")
    @JsonView(JsonDataView.SolutionManager.class)
    private Date lastEditionDate = new Date();
    @Column(name = "LEADERID")
    @JsonView(JsonDataView.SolutionManager.class)
    private Long leaderId;
    @Formula("(select pd.firstname || ' ' || pd.lastname from personal_data pd inner join appuser au on au.personaldataid=pd.id where au.ID = LEADERID)")
    @JsonView(JsonDataView.User.class)
    private String leaderName;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
}
