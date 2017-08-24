package pl.workreporter.web.beans.entities.message;

import java.util.List;

/**
 * Created by Sergiusz on 24.08.2017.
 */
public interface MessageDao {
    List<ReceivedMessage> getUserReceivedMessages(long userId);
    List<SentMessage> getUserSentMessages(long userId);
    SentMessage sendMessage(MessageParticipant senderId, List<MessageParticipant> receivers, String title, String content);
}
