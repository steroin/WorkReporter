package pl.workreporter.web.beans.entities.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.security.rest.*;
import pl.workreporter.web.service.security.PrincipalAuthenticator;

import java.util.List;

/**
 * Created by Sergiusz on 19.09.2017.
 */
@Repository
public class MessageDaoWrapper {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private PrincipalAuthenticator authenticator;

    public RestResponse<List<MessageSend>> getUserReceivedMessages(long userId) {
        try {
            if (!authenticator.authenticateUserId(userId)) {
                return new RestResponseAuthenticationError<>();
            }
            return new RestResponseSuccess<>(messageDao.getUserReceivedMessages(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<List<SentMessageWrapper>> getUserSentMessages(long userId) {
        try {
            if (!authenticator.authenticateUserId(userId)) {
                return new RestResponseAuthenticationError<>();
            }
            return new RestResponseSuccess<>(messageDao.getUserSentMessages(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseNotFoundError<>();
        }
    }

    public RestResponse<SentMessageWrapper> sendMessage(long senderId, List<MessageReceiver> receivers, String title, String content) {
        if (authenticator.authenticateUserId(senderId)) {
            for (MessageReceiver receiver : receivers) {
                if (!authenticator.authenticateSolutionId(receiver.getSolutionId())) {
                    return new RestResponseAuthenticationError<>();
                }
            }
            try {
                return new RestResponseSuccess<>(messageDao.sendMessage(senderId, receivers, title, content));
            }
            catch (Exception e) {
                e.printStackTrace();
                return new RestResponseAdditionFailedError<>();
            }
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }

    public RestResponse<List<MessageReceiver>> getAllReceiversInSolution(long solutionId) {
        if (authenticator.authenticateSolutionId(solutionId)) {
            try {
                return new RestResponseSuccess<>(messageDao.getAllReceiversInSolution(solutionId));
            } catch (Exception e) {
                e.printStackTrace();
                return new RestResponseNotFoundError<>();
            }
        } else {
            return new RestResponseAuthenticationError<>();
        }
    }
}