package pl.workreporter.web.beans.entities.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.workreporter.web.service.date.DateParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiusz on 24.08.2017.
 */
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DateParser dateParser;

    @Override
    public List<ReceivedMessage> getUserReceivedMessages(long userId) {
        String query = "select ms.id as sendid, ms.messageid, ms.receiverid, ms.senddate, ms.status," +
                "m.senderid, m.content " +
                "from message_send ms " +
                "join message m on ms.messageid=m.id " +
                "where ms.receiverid = "+userId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<ReceivedMessage> messages = new ArrayList<>();

        for (Map<String, Object> map : result) {
            ReceivedMessage msg = new ReceivedMessage();
            msg.setId(Long.parseLong(map.get("messageid").toString()));
            msg.setSenderId(Long.parseLong(map.get("senderid").toString()));
            msg.setContent(map.get("content").toString());
            msg.setReceiverId(userId);
            msg.setSendDate(dateParser.parseToReadableDate(map.get("senddate")));
            msg.setStatus(Integer.parseInt(map.get("status").toString()));
            messages.add(msg);
        }
        return messages;
    }

    @Override
    public List<SentMessage> getUserSentMessages(long userId) {
        String query = "select ms.id as sendid, ms.messageid, ms.receiverid, ms.senddate, ms.status," +
                "m.senderid, m.content " +
                "from message_send ms " +
                "join message m on ms.messageid=m.id " +
                "where m.senderid = "+userId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        Map<Long, SentMessage> messagesMap = new HashMap<>();

        for (Map<String, Object> map : result) {
            long msgId = Long.parseLong(map.get("messageId").toString());
            if (messagesMap.containsKey(msgId)) {
                messagesMap.get(msgId).getReceivers().add(Long.parseLong(map.get("receiverid").toString()));
            } else {
                SentMessage msg = new SentMessage();
                msg.setId(msgId);
                msg.setSenderId(userId);
                msg.setContent(map.get("content").toString());
                List<Long> receivers = new ArrayList<>();
                receivers.add(Long.parseLong(map.get("receiverid").toString()));
                msg.setReceivers(receivers);
                msg.setSendDate(dateParser.parseToReadableDate(map.get("senddate")));
                msg.setStatus(Integer.parseInt(map.get("status").toString()));
                messagesMap.put(msgId, msg);
            }
        }
        List<SentMessage> messagesList = new ArrayList<>();

        for (Map.Entry<Long, SentMessage> entry : messagesMap.entrySet()) {
            messagesList.add(entry.getValue());
        }
        return messagesList;
    }
}
