package pl.workreporter.web.beans.entities.message;

import pl.workreporter.web.beans.entities.user.User;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Sergiusz on 24.08.2017.
 */
@Entity
@Table(name = "MESSAGE")
public class Message implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageseq")
    @SequenceGenerator(name = "messageseq", sequenceName = "messageseq", allocationSize = 1)
    private long id;
    @ManyToOne
    @JoinColumn(name = "SENDERID")
    private User sender;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "CONTENT")
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
