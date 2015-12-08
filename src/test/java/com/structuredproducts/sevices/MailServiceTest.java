package com.structuredproducts.sevices;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Vlad on 08.12.2015.
 */
public class MailServiceTest {

    private final MailService mailService = new MailService();

    @Test
    public void test() {
        mailService.sendMessage();
    }

}
