package com.structuredproducts.sevices;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class YahooCurrencyPriceServiceTest {
    @Test
    public void getDataTest() {
        YahooCurrencyPriceService service = new YahooCurrencyPriceService();
        Map<String, String> data = service.getData("USDRUB");
        assertEquals(data.size(), 13);
    }
}
