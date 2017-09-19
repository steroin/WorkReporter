package pl.workreporter.web.beans.entities.message;


import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Sergiusz on 19.09.2017.
 */

@Entity(name = "MessageReceiver")
@Table(name = "APPUSER")
public class MessageReceiver implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appuserseq")
    @SequenceGenerator(name = "appuserseq", sequenceName = "appuserseq", allocationSize = 1)
    private long id;
    @Formula(value = "(select ac.email from account ac join appuser au on ac.id = au.accountid where au.id = id)")
    private String email;
    @Formula(value = "(select pd.firstname from personal_data pd join appuser au on pd.id = au.personaldataid where au.id = id)")
    private String firstName;
    @Formula(value = "(select pd.lastname from personal_data pd join appuser au on pd.id = au.personaldataid where au.id = id)")
    private String lastName;
    @Column(name = "SOLUTIONID")
    private long solutionId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(long solutionId) {
        this.solutionId = solutionId;
    }
}
