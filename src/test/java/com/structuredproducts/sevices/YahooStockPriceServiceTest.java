package com.structuredproducts.sevices;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Vlad on 24.03.2016.
 */
public class YahooStockPriceServiceTest {

    @Test
    public void yahooServiceTest() throws IOException {

        YahooStockPriceService service = new YahooStockPriceService();
        Map<String, String > quotes = service.getYearHistoricalQuotes("LUKOY");

        Assert.assertThat(quotes, Matchers.notNullValue());

        quotes = service.getYearHistoricalQuotes("FB");

        Assert.assertThat(quotes, Matchers.notNullValue());
    }

}
