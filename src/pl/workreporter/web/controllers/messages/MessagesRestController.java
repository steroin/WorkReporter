package pl.workreporter.web.controllers.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.workreporter.web.beans.entities.message.*;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;

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
    private MessageDaoWrapper messageDaoWrapper;

    @RequestMapping(value = "/messages/received", method = GET)
    public RestResponse<List<MessageSend>> getReceivedMessages(@RequestParam("userid") long userId) {
        return messageDaoWrapper.getUserReceivedMessages(userId);
    }

    @RequestMapping(value = "/messages/sent", method = GET)
    public RestResponse<List<SentMessageWrapper>> getSentMessages(@RequestParam("userid") long userId) {
        return messageDaoWrapper.getUserSentMessages(userId);
    }

    @RequestMapping(value = "/messages", method = POST)
    public RestResponse<SentMessageWrapper> sendMessage(@RequestBody SentMessageWrapper sentMessageWrapper) {
        return messageDaoWrapper.sendMessage(sentMessageWrapper.getMessage().getSender().getId(),
                sentMessageWrapper.getReceivers(), sentMessageWrapper.getMessage().getTitle(),
                sentMessageWrapper.getMessage().getContent());
    }

    @RequestMapping(value = "/messages/receivers", method = GET)
    public RestResponse<List<MessageReceiver>> getReceivers(@RequestParam("solutionid") long solutionId) {
        return messageDaoWrapper.getAllReceiversInSolution(solutionId);
    }
}
