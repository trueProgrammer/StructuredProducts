package com.structuredproducts.sevices;

import com.structuredproducts.persistence.entities.instrument.SystemProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
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

    @Autowired
    private DBService dbService;

    @Value( "${mail.login}" )
    private String login;
    @Value( "${mail.password}" )
    private String password;
    @Value( "${mail.service_mail_property}" )
    private String serviceMailProperty;
    private String serviceMail;

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

    @PostConstruct
    public void init() {
        SystemProperty prop = dbService.getObjectByKey(SystemProperty.class, serviceMailProperty);
        if (prop != null) {
            serviceMail = prop.getValue();
            log.debug("Service mail value: {}", serviceMail);
        } else {
            log.error("There is no system property {} in database", serviceMailProperty);
        }
    }

    public void sendMessage(String name, String midName, String secondName, String email, String phone, String text, String to) throws ServiceException {
        if (to == null) {
            to = serviceMail;
        }

        log.debug("Email [name:{}, from:{}] will be send to {}.", name, email, to);

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
