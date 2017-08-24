package pl.workreporter.web.beans.entities.message;

import java.util.List;

/**
 * Created by Sergiusz on 24.08.2017.
 */
public class SentMessage {

    private long id;
    private MessageParticipant sender;
    private String title;
    private String content;
    private List<MessageParticipant> receivers;
    private String sendDate;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MessageParticipant getSender() {
        return sender;
    }

    public void setSender(MessageParticipant sender) {
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

    public List<MessageParticipant> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<MessageParticipant> receivers) {
        this.receivers = receivers;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
