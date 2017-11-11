package pl.workreporter.web.beans.entities.personaldata;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Sergiusz on 29.08.2017.
 */
@Entity
@Table(name = "personal_data")
public class PersonalData {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personaldataseq")
    @SequenceGenerator(name = "personaldataseq", sequenceName = "personaldataseq", allocationSize = 1)
    @JsonView(JsonDataView.User.class)
    private long id;
    @Column(name = "FIRSTNAME")
    @JsonView(JsonDataView.User.class)
    private String firstName;
    @Column(name = "LASTNAME")
    @JsonView(JsonDataView.User.class)
    private String lastName;
    @Column(name = "BIRTHDAY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+1")
    @JsonView(JsonDataView.Myself.class)
    private Date birthday;
    @Column(name = "PHONE")
    @JsonView(JsonDataView.Myself.class)
    private String phone;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
