package com.structuredproducts.sevices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * Created by Vlad on 08.12.2015.
 */
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Value( "${mail.login}" )
    private String login;
    @Value( "${mail.password}" )
    private String password;

    private final Random random = new Random();

    private final Properties props;

    public MailService() {
        props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
    }

    public void sendMessage(String name, String midName, String secondName, String email, String phone, String text, String to) throws ServiceException {

        log.debug("Email [name:{}, from:{}] will be send.", name, email);

        try {
            Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login, password);
                }
            });

            mailSession.setDebug(true);

            Message msg = new MimeMessage( mailSession );

            msg.setFrom(new InternetAddress(login));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSentDate(new Date());
            msg.setSubject("Contact form " + random.nextInt());

            msg.setText(String.format("Email=[%s];\n\n" +
                                    "Телефон=[%s];\n\n" +
                                    "Имя=[%s];\n\n" +
                                    "Фамилия=[%s];\n\n" +
                                    "Отчество=[%s];\n\n" +
                                    "Вопрос='%s'\n", email, phone, name, secondName, midName, text));

            Transport.send(msg);
        } catch (MessagingException e) {
            log.error("Email [name:{}, from:{}] hasn't send.", name, email, e);
            throw new ServiceException("Email hasn't send.", e);
        }

    }

}
