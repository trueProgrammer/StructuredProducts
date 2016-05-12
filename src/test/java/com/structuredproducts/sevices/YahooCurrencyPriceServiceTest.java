package com.structuredproducts.sevices;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class YahooCurrencyPriceServiceTest {
    @Test
    public void getDataTest() throws ExecutionException {
        YahooCurrencyPriceService service = new YahooCurrencyPriceService();
        Map<String, String> data = service.getChartData("USDRUB");
        assertEquals(data.size(), 13);
    }
}
