package pl.workreporter.web.controllers.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.workreporter.web.beans.entities.message.MessageDao;
import pl.workreporter.web.beans.entities.message.Message;
import pl.workreporter.web.beans.entities.message.MessageSend;
import pl.workreporter.web.beans.entities.message.SentMessageWrapper;
import pl.workreporter.web.beans.entities.user.User;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Sergiusz on 24.08.2017.
 */
@RestController
public class MessagesRestController {

    @Autowired
    private MessageDao messageDao;

    @RequestMapping(value = "/messages/received", method = GET)
    public List<MessageSend> getReceivedMessages(@RequestParam("userid") long userId) {
        return messageDao.getUserReceivedMessages(userId);
    }

    @RequestMapping(value = "/messages/sent", method = GET)
    public List<SentMessageWrapper> getSentMessages(@RequestParam("userid") long userId) {
        return messageDao.getUserSentMessages(userId);
    }

    @RequestMapping(value = "/messages", method = POST)
    public SentMessageWrapper sendMessage(@RequestBody SentMessageWrapper sentMessageWrapper) {
        return messageDao.sendMessage(sentMessageWrapper.getMessage().getSender().getId(), sentMessageWrapper.getReceivers(), sentMessageWrapper.getMessage().getTitle(), sentMessageWrapper.getMessage().getContent());
    }
}
