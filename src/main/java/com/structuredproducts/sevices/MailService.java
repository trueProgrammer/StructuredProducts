package com.structuredproducts.sevices;

import org.apache.commons.lang3.StringUtils;
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

    //this is main email, that contains all mails and all mails goes via it
    @Value( "${mail.login}" )
    private String login;
    @Value( "${mail.password}" )
    private String password;

    private final Random random = new Random();
    private final Properties props;

    private static final String QUESTION_SUBJECT = "xstrum.ru: Вопрос.";
    private static final String REQUEST_SUBJECT = "xstrum.ru: Заявка на создание продукта.";
    private static final String COMMA = ",";


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

    private void sendEmail(final String subject, final String text, final String recipients) throws MessagingException {
        Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        });
        mailSession.setDebug(true);

        Message msg = new MimeMessage( mailSession );

        msg.setFrom(new InternetAddress(login));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(login + COMMA + recipients));
        msg.setSentDate(new Date());
        msg.setSubject(subject + random.nextInt());
        msg.setText(text);

        Transport.send(msg);
    }

    public void sendQuestion(String name, String midName, String secondName, String email, String phone, String text, String serviceEmail) throws ServiceException {
        log.debug("Question [name:{}, from:{}] will be send.", name, email);

        try {
            final String emailBody = String.format(
                    "Данные клиента\n\n" +
                    "\tEmail: %s;\n\n" +
                    "\tТелефон: %s;\n\n" +
                    "\tФамилия: %s;\n\n" +
                    "\tИмя: %s;\n\n" +
                    "\tОтчество: %s;\n\n" +
                    "\tВопрос: %s;\n\n"
                    , email, phone, secondName, name, midName, text);

                    sendEmail(QUESTION_SUBJECT, emailBody, serviceEmail != null ? serviceEmail : StringUtils.EMPTY);
        } catch (MessagingException e) {
            log.error("Email [name:{}, from:{}] hasn't send.", name, email, e);
            throw new ServiceException("Email hasn't send.", e);
        }

    }

    public void sendRequest(String name, String midName, String email, String phone, String to,
                            String profitBlock, String time, String invest, String risk,
                            String broker, String period, String under, String strategy) throws ServiceException {
        log.debug("Request [name:{}, from:{}] will be send to {}.", name, email, to);

        try {
            String emailBody = String.format("Заявка на создание структурного продукта с сервиса xstrum.ru\n\n\n" +
                                "Данные клиента\n\n" +
                                "\tEmail: %s;\n\n" +
                                "\tТелефон: %s;\n\n" +
                                "\tИмя: %s;\n\n" +
                                "\tФамилия: %s;\n\n"
                                , email, phone, name, midName);

            emailBody += String.format("\n\nПараметры структурного продукта для создания\n\n\n" +
                    "\tДоходность: %s;\n\n" +
                    "\tСрок инвестирования: %s;\n\n" +
                    "\tСумма инвестиций: %s;\n\n" +
                    "\tУровень защиты инвестиций: %s;\n\n" +
                    "\tПровайдер продукта (брокер): %s;\n\n" +
                    "\tПериодичность выплат: %s;\n\n" +
                    "\tБазовый актив: %s;\n\n" +
                    "\tСтратегия: %s;\n\n"
                    , profitBlock, time, invest, risk,
                    broker, period, under, strategy);

            sendEmail(REQUEST_SUBJECT, emailBody, to);
        } catch (MessagingException e) {
            log.error("Email [name:{}, from:{}] hasn't send.", name, email, e);
            throw new ServiceException("Email hasn't send.", e);
        }
    }
}
