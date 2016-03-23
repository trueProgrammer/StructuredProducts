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
    public void testMSFT() throws IOException {
        Calendar from = Calendar.getInstance();
        from.set(Calendar.MONTH, Calendar.JANUARY);
        from.set(Calendar.DAY_OF_MONTH, 1);
        from.set(Calendar.YEAR, 2005);

        Calendar to = Calendar.getInstance();
        to.set(Calendar.MONTH, Calendar.DECEMBER);
        to.set(Calendar.DAY_OF_MONTH, 31);
        to.set(Calendar.YEAR, 2009);

        Map<Date, Double> quotes = YahooUnderlayingPriceService.getHistoricalQuotes("MSFT", from.getTime(), to.getTime());

        Assert.assertThat(quotes, Matchers.aMapWithSize(1259));
    }

}
