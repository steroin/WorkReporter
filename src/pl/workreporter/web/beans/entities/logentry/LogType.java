package pl.workreporter.web.beans.entities.logentry;

import com.fasterxml.jackson.annotation.JsonView;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Sergiusz on 22.08.2017.
 */
@Entity
@Table(name = "log_type")
public class LogType implements Serializable {
    @Id
    @Column(name = "id")
    @JsonView(JsonDataView.User.class)
    private long id;
    @Column(name = "name")
    @JsonView(JsonDataView.User.class)
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
