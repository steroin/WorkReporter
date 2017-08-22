package pl.workreporter.web.beans.entities.logentry;

/**
 * Created by Sergiusz on 22.08.2017.
 */
public class LogType {
    private long id;
    private String name;

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
}
