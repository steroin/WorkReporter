package pl.workreporter.web.beans.entities.logentry;

/**
 * Created by Sergiusz on 21.08.2017.
 */
public class LogEntry {
    private long id;
    private long userId;
    private long teamId;
    private long logTypeId;
    private Long projectId;
    private String startHour;
    private double loggedHours;
    private String day;
    private String logDate;
    private String lastEditionDate;
    private int status;
    private Long acceptedBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getLogTypeId() {
        return logTypeId;
    }

    public void setLogTypeId(long logTypeId) {
        this.logTypeId = logTypeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public double getLoggedHours() {
        return loggedHours;
    }

    public void setLoggedHours(double loggedHours) {
        this.loggedHours = loggedHours;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLastEditionDate() {
        return lastEditionDate;
    }

    public void setLastEditionDate(String lastEditionDate) {
        this.lastEditionDate = lastEditionDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(Long acceptedBy) {
        this.acceptedBy = acceptedBy;
    }
}
