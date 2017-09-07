package pl.workreporter.web.beans.entities.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.workreporter.web.beans.entities.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sergiusz on 29.08.2017.
 */
@Entity
@Table(name = "MESSAGE_SEND")
public class MessageSend implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messagesendseq")
    @SequenceGenerator(name = "messagesendseq", sequenceName = "messagesendseq", allocationSize = 1)
    private long id;
    @ManyToOne
    @JoinColumn(name = "MESSAGEID")
    private Message message;
    @ManyToOne
    @JoinColumn(name = "RECEIVERID")
    private User receiver;
    @Column(name = "SENDDATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+2")
    private Date sendDate = new Date();
    @Column(name = "STATUS")
    private int status = 1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
