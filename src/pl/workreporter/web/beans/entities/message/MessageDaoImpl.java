package pl.workreporter.web.beans.entities.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import pl.workreporter.web.beans.entities.user.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by Sergiusz on 24.08.2017.
 */
@Repository
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Override
    public List<MessageSend> getUserReceivedMessages(long userId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MessageSend> query = criteriaBuilder.createQuery(MessageSend.class);
        Root<MessageSend> root = query.from(MessageSend.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("receiver"), entityManager.find(User.class, userId)));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<SentMessageWrapper> getUserSentMessages(long userId) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MessageSend> query = criteriaBuilder.createQuery(MessageSend.class);
        Root<MessageSend> root = query.from(MessageSend.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("message").get("sender"), entityManager.find(User.class, userId)));
        List<MessageSend> sends = entityManager.createQuery(query).getResultList();
        Map<Message, SentMessageWrapper> map = new HashMap<>();

        for (MessageSend send : sends) {
            Message message = send.getMessage();
            User receiver = send.getReceiver();

            if (!map.containsKey(message)) {
                List<User> receivers = new ArrayList<>();
                receivers.add(receiver);
                SentMessageWrapper sentMessageWrapper = new SentMessageWrapper();
                sentMessageWrapper.setMessage(message);
                sentMessageWrapper.setReceivers(receivers);
                sentMessageWrapper.setSendDate(send.getSendDate());
                map.put(message, sentMessageWrapper);
            } else {
                map.get(message).getReceivers().add(receiver);
            }
        }

        return new ArrayList<>(map.values());
    }

    @Override
    public SentMessageWrapper sendMessage(long senderId, List<User> receivers, String title, String content) {
        EntityManager entityManager = entityManagerFactoryBean.getObject().createEntityManager();
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setSender(entityManager.find(User.class, senderId));

        entityManager.getTransaction().begin();
        entityManager.persist(message);
        Date sendDate = null;

        for (User receiver : receivers) {
            MessageSend messageSend = new MessageSend();
            messageSend.setMessage(message);
            messageSend.setReceiver(receiver);
            if (sendDate == null) {
                sendDate = messageSend.getSendDate();
            }
            entityManager.persist(messageSend);
        }
        entityManager.getTransaction().commit();

        SentMessageWrapper sentMessageWrapper = new SentMessageWrapper();
        sentMessageWrapper.setMessage(message);
        sentMessageWrapper.setReceivers(receivers);
        sentMessageWrapper.setSendDate(sendDate);

        return sentMessageWrapper;
    }
}
