package pl.workreporter.web.beans.entities.message;

import java.util.List;

/**
 * Created by Sergiusz on 24.08.2017.
 */
public class SentMessage {

    private long id;
    private long senderId;
    private String content;
    private List<Long> receivers;
    private String sendDate;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Long> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Long> receivers) {
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
