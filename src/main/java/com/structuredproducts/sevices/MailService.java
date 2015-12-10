package com.structuredproducts.sevices;

import org.apache.log4j.Logger;
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

    private static final Logger log = Logger.getLogger(MailService.class);

    @Value( "${mail.login}" )
    private String login;
    @Value( "$mail.password" )
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



    public void sendMessage() throws ServiceException {

        log.debug("Email will be send");

        try {
            Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login, password);
                }
            });

            mailSession.setDebug(true);

            Message msg = new MimeMessage( mailSession );

            msg.setFrom(new InternetAddress(login));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(login));
            msg.setSentDate(new Date());
            msg.setSubject("Test" + random.nextInt());

            msg.setText( "Test e-mail sent with using JavaMail" );

            Transport.send( msg );
        } catch (MessagingException e) {
            log.error("Email hasn't send.", e);
            throw new ServiceException("Email hasn't send.", e);
        }

    }

}
