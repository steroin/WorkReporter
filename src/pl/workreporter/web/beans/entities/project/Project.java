package pl.workreporter.web.beans.entities.project;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sergiusz on 15.08.2017.
 */
public class Project {
    private long id;
    private long solutionId;
    private String name;
    private String description;
    private String creationDate;
    private String lastEditionDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(long solutionId) {
        this.solutionId = solutionId;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        this.creationDate = sdf.format(creationDate);
    }

    public String getLastEditionDate() {
        return lastEditionDate;
    }

    public void setLastEditionDate(Date lastEditionDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        this.lastEditionDate = sdf.format(lastEditionDate);
    }
}
