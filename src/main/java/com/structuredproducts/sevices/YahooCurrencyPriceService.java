package com.structuredproducts.sevices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class YahooCurrencyPriceService extends HistoricalCachingDataService{
    private final static Logger logger = LoggerFactory.getLogger(YahooCurrencyPriceService.class);

    private static final String CURRENCY_TYPE = "валюта";
    private static final String CURRENCY_URL_SUFFIX = "=X";

    /*private static final String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December" };*/
    private static final String[] MONTH_NAMES = { "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль",
            "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" };
    //private final static String URL = "https://finance-yql.media.yahoo.com/v7/finance/chart/%s=X?period1=%d&period2=%d&interval=1d&indicators=quote&includeTimestamps=true&includePrePost=true&events=div%%7Csplit%%7Cearn&corsDomain=finance.yahoo.com";
    private final static String URL = "https://finance-yql.media.yahoo.com/v7/finance/chart/%s?period1=%d&period2=%d&interval=1d&indicators=quote&includeTimestamps=true&includePrePost=true&events=div%%7Csplit%%7Cearn&corsDomain=finance.yahoo.com";

    protected String prepareUrl(String urlTemplate, String symbol, String type, Calendar startDate, Calendar endDate) {
        if (CURRENCY_TYPE.equalsIgnoreCase(type)) {
            symbol += CURRENCY_URL_SUFFIX;
        }
        return String.format(urlTemplate, symbol, startDate.getTimeInMillis() / 1000, endDate.getTimeInMillis() / 1000);
    }

    protected Multimap<Date, String> parseData(InputStream inputStream) throws IOException {
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(inputStream, stringWriter);
        String data = stringWriter.toString();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object jsonObj = mapper.readValue(data, Object.class);
            Object result = ((List) PropertyUtils.getProperty(jsonObj, "chart.result")).get(0);
            Object quote = ((List) PropertyUtils.getProperty(result, "indicators.quote")).get(0);

            List timestamps = ((List) PropertyUtils.getProperty(result, "timestamp"));
            List closePrices = ((List) PropertyUtils.getProperty(quote, "close"));

            //Map<String, String> map = new LinkedHashMap<>();
            Multimap<Date, String> map = LinkedListMultimap.create();
            if (timestamps.size() == closePrices.size()) {
                for (int i = 0; i < timestamps.size(); i++) {
                    if (closePrices.get(i) == null) {
                        continue;
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date((Long.parseLong(timestamps.get(i).toString())) * 1000));
                    //map.put(MONTH_NAMES[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR), closePrices.get(i).toString());
                    //map.put(String.valueOf((Long.parseLong(timestamps.get(i).toString())) * 1000), closePrices.get(i).toString());
                    map.put(new Date((Long.parseLong(timestamps.get(i).toString())) * 1000), closePrices.get(i).toString());
                }
            }
            return map;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("Unexpected format of json {}", data);
            logger.error("", e);
        } catch (IOException e) {
            logger.error("Cannot read json to object", e);
        }
        return ArrayListMultimap.create();
    }

    @Override
    protected String getUrl() {
        return URL;
    }
}
