package com.structuredproducts.sevices;

import com.structuredproducts.persistence.entities.instrument.Product;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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

    private static final String QUESTION_SUBJECT = "Экструм: вопрос от клиента.";
    private static final String CREATE_REQUEST_SUBJECT = "Эструм: заявка на создание структурного продукта.";
    private static final String REQUEST_SUBJECT = "Эструм: заявка на покупку структурного продукта.";
    private static final String COMMA = ",";
    private static final String SIGNATURE = "<br/><br/><b>С уважением, Ваш Экструм.</b><br>xstrum.ru | Email: order@xstrum.ru";

    public MailService() {
        props = new Properties();

        props.put("mail.smtp.host", "smtp.mail.ru");
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

        MimeMessage  msg = new MimeMessage( mailSession );

        msg.setFrom(new InternetAddress(login));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(login + COMMA + recipients));
        msg.setSentDate(new Date());
        msg.setSubject(subject + random.nextInt());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(text + SIGNATURE, "UTF-8","html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        msg.setContent(multipart);

        Transport.send(msg);
    }

    public void sendQuestion(String name, String midName, String secondName, String email, String phone, String text, String serviceEmail) throws ServiceException {
        log.debug("Question [name:{}, from:{}] will be send.", name, email);
        try {
            final String emailBody = String.format(
                    "Данные клиента<br>" +
                    "Email: %s;<br>" +
                    "Телефон: %s;<br>" +
                    "Фамилия: %s;<br>" +
                    "Имя: %s;<br>" +
                    "Отчество: %s;<br>" +
                    "Вопрос: %s;<br>"
                    , email, phone, secondName, name, midName, text);

                    sendEmail(QUESTION_SUBJECT, emailBody, serviceEmail != null ? serviceEmail : StringUtils.EMPTY);
        } catch (MessagingException e) {
            log.error("Email [name:{}, from:{}] hasn't send.", name, email, e);
            throw new ServiceException("Email hasn't send.", e);
        }
    }

    public void sendRequest(String name, String midName, String email, String phone, String to,
                            Product product) throws ServiceException {
        log.debug("Request [name:{}, from:{}] will be send to {}.", name, email, product.getBroker().getName());
        try {
            String emailBody = String.format("Добрый день! <br> %s %s оставил заявку на покупку продукта " +
                                                "\"%s\" <a href=\"http://xstrum.ru/#/product?id=%s\">ссылка на страницу продукта</a>",
                                            name, midName, product.getName(), product.getId());
            emailBody += String.format("<br><br>Данные клиента:<br>" +
                            "Email: %s;<br>" +
                            "Телефон: %s;<br>."
                            , email, phone);
            sendEmail(REQUEST_SUBJECT, emailBody, to);
        } catch (MessagingException e) {
            log.error("Email [name:{}, from:{}] hasn't send.", name, email, e);
            throw new ServiceException("Email hasn't send.", e);
        }
    }

    public void sendCreateRequest(String name, String midName, String email, String phone, String to,
                                  String profitBlock, String time, String invest, String risk,
                                  String broker, String period, String under, String strategy) throws ServiceException {
        log.debug("Create Request [name:{}, from:{}] will be send to {}.", name, email, to);

        try {
            String emailBody = String.format("Добрый день!<br><br>" +
                                "%s %s оставил заявку на создание инвестиционного продукта на сайте <a href=\"http://xstrum.ru\">Экструм</a>."
                                , name, midName);
            emailBody += String.format("<br><br>Данные клиента:<br>" +
                            "Email: %s;<br>" +
                            "Телефон: %s.<br>"
                            , email, phone);
            emailBody += String.format("<br><br>Перечень параметров, которые заполнил клиент:<br>" +
                            "Доходность: %s;<br>" +
                            "Срок инвестирования: %s;<br>" +
                            "Сумма инвестиций: %s;<br>" +
                            "Уровень защиты инвестиций: %s;<br>"
                            , profitBlock, time, invest, risk);

            if (StringUtils.isNotEmpty(broker)) {
                emailBody += "<br>Провайдер продукта (брокер): " + broker;
            }
            if (StringUtils.isNotEmpty(period)) {
                emailBody += "<br>Периодичность выплат: " + period;
            }
            if (StringUtils.isNotEmpty(under)) {
                emailBody += "<br>Базовый актив: " + under;
            }
            if (StringUtils.isNotEmpty(strategy)) {
                emailBody += "</br>Стратегия: " + strategy;
            }
            sendEmail(CREATE_REQUEST_SUBJECT, emailBody, to);
        } catch (MessagingException e) {
            log.error("Email [name:{}, from:{}] hasn't send.", name, email, e);
            throw new ServiceException("Email hasn't send.", e);
        }
    }
}
