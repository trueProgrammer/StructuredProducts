package com.structuredproducts.sevices;

import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Vlad on 08.12.2015.
 */
public class MailService {

    @Value( "${mail.login}" )
    private String login = "vl.isaev@mail.ru";
    @Value( "$mail.password" )
    private String password = "17935totalWarpony019vis";

    //yulia.ser.balashova@gmail.com
    //9841silent_hill9841

    private final Properties props;

    public MailService() {
        props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com"); // for gmail use smtp.gmail.com
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
    }



    public void sendMessage() {
        System.out.println(login);
        System.out.println(password);

        try {
            Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("yulia.ser.balashova@gmail.com", "9841silent_hill9841");
                }
            });

            mailSession.setDebug(true); // Enable the debug mode

            Message msg = new MimeMessage( mailSession );

            //--[ Set the FROM, TO, DATE and SUBJECT fields
            msg.setFrom( new InternetAddress( "test@mail.ru" ) );
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("yulia.ser.balashova@gmail.com"));
            msg.setSentDate(new Date());
            msg.setSubject( "Test" );

            //--[ Create the body of the mail
            msg.setText( "Test e-mail sent with JavaMail" );

            //--[ Ask the Transport class to send our mail message
            Transport.send( msg );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

}
