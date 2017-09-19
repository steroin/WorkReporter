package pl.workreporter.web.beans.entities.message;

import pl.workreporter.web.beans.entities.user.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 24.08.2017.
 */
public interface MessageDao {
    List<MessageSend> getUserReceivedMessages(long userId);
    List<SentMessageWrapper> getUserSentMessages(long userId);
    SentMessageWrapper sendMessage(long senderId, List<MessageReceiver> receivers, String title, String content);
    List<MessageReceiver> getAllReceiversInSolution(long solutionId);
}
