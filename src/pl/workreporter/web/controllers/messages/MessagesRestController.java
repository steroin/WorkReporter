package pl.workreporter.web.controllers.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.workreporter.web.beans.entities.message.MessageDao;
import pl.workreporter.web.beans.entities.message.MessageParticipant;
import pl.workreporter.web.beans.entities.message.ReceivedMessage;
import pl.workreporter.web.beans.entities.message.SentMessage;

import java.util.ArrayList;
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
    public List<ReceivedMessage> getReceivedMessages(@RequestParam("userid") long userId) {
        return messageDao.getUserReceivedMessages(userId);
    }

    @RequestMapping(value = "/messages/sent", method = GET)
    public List<SentMessage> getSentMessages(@RequestParam("userid") long userId) {
        return messageDao.getUserSentMessages(userId);
    }

    @RequestMapping(value = "/messages", method = POST)
    public SentMessage sendMessage(@RequestBody SentMessage message) {
        return messageDao.sendMessage(message.getSender(), message.getReceivers(), message.getTitle(), message.getContent());
    }
}
