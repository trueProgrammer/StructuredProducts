package com.structuredproducts.sevices;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.junit.Assert.assertThat;

public class YahooCurrencyPriceServiceTest {
    @Test
    public void getDataTest() throws ExecutionException {
        YahooCurrencyPriceService service = new YahooCurrencyPriceService();

        Map<String, String> data = service.getHistoricalCachingData("USDRUB");
        assertThat(data, aMapWithSize(12));
    }
}
