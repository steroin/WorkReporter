package pl.workreporter.web.beans.entities.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.service.date.DateParser;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Sergiusz on 24.08.2017.
 */
@Repository
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DateParser dateParser;

    @Override
    public List<ReceivedMessage> getUserReceivedMessages(long userId) {
        String query = "select ms.id as sendid, ms.messageid, ms.receiverid, ms.senddate, ms.status," +
                "m.senderid, m.title, m.content, senderacc.email as senderemail, senderpd.firstname as senderfirstname, " +
                "senderpd.lastname as senderlastname, receiveracc.email as receiveremail, " +
                "receiverpd.firstname as receiverfirstname, receiverpd.lastname as receiverlastname " +
                "from message_send ms " +
                "join message m on ms.messageid=m.id " +
                "join appuser sender on m.senderid=sender.id " +
                "join account senderacc on sender.accountid=senderacc.id " +
                "join personal_data senderpd on sender.personaldataid=senderpd.id " +
                "join appuser receiver on ms.receiverid=receiver.id " +
                "join account receiveracc on receiver.accountid=receiveracc.id " +
                "join personal_data receiverpd on receiver.personaldataid=receiverpd.id " +
                "where ms.receiverid = "+userId;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<ReceivedMessage> messages = new ArrayList<>();

        for (Map<String, Object> map : result) {
            MessageParticipant sender = new MessageParticipant();
            sender.setId(Long.parseLong(map.get("senderid").toString()));
            sender.setFirstName(map.get("senderfirstname").toString());
            sender.setLastName(map.get("senderlastname").toString());
            sender.setEmail(map.get("senderemail").toString());


            MessageParticipant receiver = new MessageParticipant();
            receiver.setId(userId);
            receiver.setFirstName(map.get("receiverfirstname").toString());
            receiver.setLastName(map.get("receiverlastname").toString());
            receiver.setEmail(map.get("receiveremail").toString());

            ReceivedMessage msg = new ReceivedMessage();
            msg.setId(Long.parseLong(map.get("messageid").toString()));
            msg.setSender(sender);
            msg.setTitle(map.get("title").toString());
            msg.setContent(map.get("content").toString());
            msg.setReceiver(receiver);
            msg.setSendDate(dateParser.parseToReadableDate(map.get("senddate")));
            msg.setStatus(Integer.parseInt(map.get("status").toString()));
            messages.add(msg);
        }
        return messages;
    }

    @Override
    public List<SentMessage> getUserSentMessages(long userId) {
        String query = "select ms.id as sendid, ms.messageid, ms.receiverid, ms.senddate, ms.status," +
                "m.senderid, m.title, m.content, senderacc.email as senderemail, senderpd.firstname as senderfirstname, " +
                "senderpd.lastname as senderlastname, receiveracc.email as receiveremail, " +
                "receiverpd.firstname as receiverfirstname, receiverpd.lastname as receiverlastname " +
                "from message_send ms " +
                "join message m on ms.messageid=m.id " +
                "join appuser sender on m.senderid=sender.id " +
                "join account senderacc on sender.accountid=senderacc.id " +
                "join personal_data senderpd on sender.personaldataid=senderpd.id " +
                "join appuser receiver on ms.receiverid=receiver.id " +
                "join account receiveracc on receiver.accountid=receiveracc.id " +
                "join personal_data receiverpd on receiver.personaldataid=receiverpd.id " +
                "where m.senderid = "+userId;

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        Map<Long, SentMessage> messagesMap = new HashMap<>();

        for (Map<String, Object> map : result) {
            MessageParticipant receiver = new MessageParticipant();
            receiver.setId(userId);
            receiver.setFirstName(map.get("receiverfirstname").toString());
            receiver.setLastName(map.get("receiverlastname").toString());
            receiver.setEmail(map.get("receiveremail").toString());

            long msgId = Long.parseLong(map.get("messageId").toString());

            if (messagesMap.containsKey(msgId)) {
                messagesMap.get(msgId).getReceivers().add(receiver);
            } else {
                MessageParticipant sender = new MessageParticipant();
                sender.setId(Long.parseLong(map.get("senderid").toString()));
                sender.setFirstName(map.get("senderfirstname").toString());
                sender.setLastName(map.get("senderlastname").toString());
                sender.setEmail(map.get("senderemail").toString());

                SentMessage msg = new SentMessage();
                msg.setId(msgId);
                msg.setSender(sender);
                msg.setTitle(map.get("title").toString());
                msg.setContent(map.get("content").toString());
                List<MessageParticipant> receivers = new ArrayList<>();
                receivers.add(receiver);
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

    @Override
    public SentMessage sendMessage(MessageParticipant sender, List<MessageParticipant> receivers, String title, String content) {
        StringBuilder queryBuilder = new StringBuilder();
        String query = "select messageseq.nextval as messageid from dual";
        long messageId = Long.parseLong(jdbcTemplate.queryForMap(query).get("messageid").toString());
        queryBuilder.append("begin \n");
        queryBuilder.append("insert into message (id, senderid, content, title) " +
                "values ("+messageId+", "+sender.getId()+", '"+content+"', '"+title+"'); \n");
        for (MessageParticipant receiver : receivers) {
            queryBuilder.append("insert into message_send (id, messageid, receiverid, senddate, status) " +
                    "values(messagesendseq.nextval, "+messageId+", "+receiver.getId()+", sysdate, 1); \n");
        }
        queryBuilder.append("end;");
        jdbcTemplate.execute(queryBuilder.toString());

        SentMessage message = new SentMessage();
        message.setId(messageId);
        message.setSender(sender);
        message.setTitle(title);
        message.setContent(content);
        message.setReceivers(receivers);
        message.setStatus(1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        message.setSendDate(sdf.format(new Date()));
        return message;
    }
}
