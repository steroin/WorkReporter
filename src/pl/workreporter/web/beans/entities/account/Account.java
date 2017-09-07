package pl.workreporter.web.beans.entities.account;

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
    private long id;
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "PASSWORD")
    private String hashedPassword;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "STATUS")
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
