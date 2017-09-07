package pl.workreporter.web.beans.entities.project;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import pl.workreporter.web.beans.entities.solution.Solution;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sergiusz on 15.08.2017.
 */
@Entity
@Table(name = "PROJECT")
public class Project implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectseq")
    @SequenceGenerator(name = "projectseq", sequenceName = "projectseq", allocationSize = 1)
    private long id;
    @ManyToOne
    @JoinColumn(name = "SOLUTIONID")
    @JsonBackReference(value = "solutionProjects")
    private Solution solution;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+2")
    private Date creationDate = new Date();
    @Column(name = "LAST_EDITION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+2")
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
