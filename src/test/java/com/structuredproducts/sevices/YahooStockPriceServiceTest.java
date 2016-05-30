package com.structuredproducts.sevices;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.junit.Assert.assertThat;

/**
 * Created by Vlad on 24.03.2016.
 */
public class YahooStockPriceServiceTest {

    @Test
    public void yahooServiceTest() throws IOException, ExecutionException {
        YahooStockPriceService service = new YahooStockPriceService();

        Map<String, String > quotes = service.getHistoricalCachingData("LUKOY");
        assertThat(quotes, aMapWithSize(12));

        quotes = service.getHistoricalCachingData("FB");
        assertThat(quotes, aMapWithSize(12));
    }

}
