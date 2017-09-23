package pl.workreporter.web.beans.entities.account;

import com.fasterxml.jackson.annotation.JsonView;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

import javax.persistence.*;

/**
 * Created by Sergiusz on 29.08.2017.
 */
@Entity
@Table(name = "ACCOUNT")
public class Account {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountseq")
    @SequenceGenerator(name = "accountseq", sequenceName = "accountseq", allocationSize = 1)
    @JsonView(JsonDataView.Myself.class)
    private long id;
    @Column(name = "LOGIN")
    @JsonView(JsonDataView.Myself.class)
    private String login;
    @Column(name = "PASSWORD")
    @JsonView(JsonDataView.Myself.class)
    private String hashedPassword;
    @Column(name = "EMAIL")
    @JsonView(JsonDataView.Myself.class)
    private String email;
    @Column(name = "STATUS")
    @JsonView(JsonDataView.Myself.class)
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
