package pl.workreporter.web.beans.entities.solution;

import java.util.Date;
import java.util.List;

/**
 * Created by Sergiusz on 12.08.2017.
 */
public class Solution {
    private int id;
    private String name;
    private Date creationDate;
    private Date lastEditionDate;
    private List<Integer> administrators;
    private List<Integer> projects;
    private List<Integer> teams;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<Integer> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<Integer> administrators) {
        this.administrators = administrators;
    }

    public List<Integer> getProjects() {
        return projects;
    }

    public void setProjects(List<Integer> projects) {
        this.projects = projects;
    }

    public List<Integer> getTeams() {
        return teams;
    }

    public void setTeams(List<Integer> teams) {
        this.teams = teams;
    }
}
