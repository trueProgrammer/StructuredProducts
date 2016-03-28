package com.structuredproducts.sevices;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.Matchers.is;

/**
 * Created by Vlad on 24.03.2016.
 */
public class YahooUnderlayingPriceServiceTest {

    @Test
    public void yahooServiceTest() throws IOException {

        Map<String, String > quotes = YahooUnderlayingPriceService.getYearHistoricalQuotes("LUKOY");

        Assert.assertThat(quotes, Matchers.notNullValue());

        quotes = YahooUnderlayingPriceService.getYearHistoricalQuotes("FB");

        Assert.assertThat(quotes, Matchers.notNullValue());
    }

}
