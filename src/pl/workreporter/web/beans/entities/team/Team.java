package pl.workreporter.web.beans.entities.team;

import java.util.List;

/**
 * Created by Sergiusz on 19.08.2017.
 */
public class Team {
    private long id;
    private long solutionId;
    private String name;
    private String creationDate;
    private String lastEditionDate;
    private List<Long> teamAdministrators;

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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastEditionDate() {
        return lastEditionDate;
    }

    public void setLastEditionDate(String lastEditionDate) {
        this.lastEditionDate = lastEditionDate;
    }

    public List<Long> getTeamAdministrators() {
        return teamAdministrators;
    }

    public void setTeamAdministrators(List<Long> teamAdministrators) {
        this.teamAdministrators = teamAdministrators;
    }
}
