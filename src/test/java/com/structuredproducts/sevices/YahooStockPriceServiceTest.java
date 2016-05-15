package com.structuredproducts.sevices;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Vlad on 24.03.2016.
 */
public class YahooStockPriceServiceTest {

    @Test
    public void yahooServiceTest() throws IOException {

        YahooStockPriceService service = new YahooStockPriceService();
        Map<String, String > quotes = service.getYearHistoricalQuotes("LUKOY");

        assertThat(quotes, notNullValue());

        quotes = service.getYearHistoricalQuotes("FB");

        assertThat(quotes, notNullValue());
    }

}
