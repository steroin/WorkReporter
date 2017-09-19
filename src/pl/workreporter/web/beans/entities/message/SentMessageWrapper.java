package pl.workreporter.web.beans.entities.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.workreporter.web.beans.entities.user.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Sergiusz on 04.09.2017.
 */
public class SentMessageWrapper {
    private Message message;
    private List<MessageReceiver> receivers;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT+2")
    private Date sendDate;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<MessageReceiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<MessageReceiver> receivers) {
        this.receivers = receivers;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}
