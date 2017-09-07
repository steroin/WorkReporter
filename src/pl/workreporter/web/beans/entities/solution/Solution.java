package pl.workreporter.web.beans.entities.solution;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import pl.workreporter.web.beans.entities.position.Position;
import pl.workreporter.web.beans.entities.project.Project;
import pl.workreporter.web.beans.entities.team.Team;
import pl.workreporter.web.beans.entities.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by Sergiusz on 12.08.2017.
 */
@Entity
@Table(name = "SOLUTION")
public class Solution implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solutionseq")
    @SequenceGenerator(name = "solutionseq", sequenceName = "solutionseq", allocationSize = 1)
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CREATION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date creationDate = new Date();
    @Column(name = "LAST_EDITION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date lastEditionDate = new Date();
    @OneToMany(mappedBy = "solution")
    @JsonManagedReference(value = "solutionProjects")
    private Set<Project> projects;
    @OneToMany(mappedBy = "solution")
    @JsonManagedReference(value = "solutionPositions")
    private Set<Position> positions;
    @OneToMany(mappedBy = "solution")
    @JsonManagedReference(value = "solutionTeams")
    private Set<Team> teams;
    @OneToMany(mappedBy = "solution")
    @JsonManagedReference(value = "solutionEmployees")
    private Set<User> employees;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Set<User> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<User> employees) {
        this.employees = employees;
    }
}
