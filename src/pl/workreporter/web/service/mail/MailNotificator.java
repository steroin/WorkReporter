package pl.workreporter.web.service.mail;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Sergiusz on 19.08.2017.
 */
@Service
public class MailNotificator {
    public void sendInitialMessage(String login, String password, String email) {
        final String sourceUsername = "noreply.workreporter@gmail.com";
        final String sourcePassword = "workreporter1";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sourceUsername, sourcePassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("workreporter@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("WorkReporter - dane do logowania");
            message.setText("Dane do logowania w systemie WorkReporter:"
                    + "\n\n Login: "+login+"\nHasło: "+password);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

