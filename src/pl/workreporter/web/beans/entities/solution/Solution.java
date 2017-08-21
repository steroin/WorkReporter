package pl.workreporter.web.beans.entities.solution;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Sergiusz on 12.08.2017.
 */
public class Solution {
    private int id;
    private String name;
    private String creationDate;
    private String lastEditionDate;
    private List<Long> administrators;
    private List<Long> projects;
    private List<Long> positions;
    private List<Long> teams;
    private List<Long> employees;

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

    public List<Long> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<Long> administrators) {
        this.administrators = administrators;
    }

    public List<Long> getProjects() {
        return projects;
    }

    public void setProjects(List<Long> projects) {
        this.projects = projects;
    }

    public List<Long> getPositions() {
        return positions;
    }

    public void setPositions(List<Long> positions) {
        this.positions = positions;
    }

    public List<Long> getTeams() {
        return teams;
    }

    public void setTeams(List<Long> teams) {
        this.teams = teams;
    }

    public List<Long> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Long> employees) {
        this.employees = employees;
    }
}
