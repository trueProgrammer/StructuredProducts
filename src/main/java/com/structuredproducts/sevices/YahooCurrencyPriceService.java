package com.structuredproducts.sevices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.structuredproducts.persistence.entities.instrument.UnderlayingPeriod;
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
    private static final String[] DAYS_OF_WEEK = { "Вск", "Пон", "Вт", "Ср", "Чет", "Пят", "Суб"};
    //private final static String URL = "https://finance-yql.media.yahoo.com/v7/finance/chart/%s=X?period1=%d&period2=%d&interval=1d&indicators=quote&includeTimestamps=true&includePrePost=true&events=div%%7Csplit%%7Cearn&corsDomain=finance.yahoo.com";
    private final static String URL_YEAR = "https://finance-yql.media.yahoo.com/v7/finance/chart/%s?period1=%d&period2=%d&interval=1d&indicators=quote&includeTimestamps=true&includePrePost=true&events=div%%7Csplit%%7Cearn&corsDomain=finance.yahoo.com";
    private final static String URL_5D = "https://finance-yql.media.yahoo.com/v7/finance/chart/%s?period1=%d&period2=%d&interval=5m&indicators=quote&includeTimestamps=true&includePrePost=true&events=div%%7Csplit%%7Cearn&corsDomain=finance.yahoo.com";

    protected String prepareUrl(String urlTemplate, String symbol, String type, Calendar startDate, Calendar endDate) {
        if (CURRENCY_TYPE.equalsIgnoreCase(type)) {
            symbol += CURRENCY_URL_SUFFIX;
        }
        return String.format(urlTemplate, symbol, startDate.getTimeInMillis() / 1000, endDate.getTimeInMillis() / 1000);
    }

    protected Multimap<?, String> parseData(InputStream inputStream, String underlayingPeriod) throws IOException {
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

            Multimap<Object, String> map = LinkedListMultimap.create();
            if (timestamps.size() == closePrices.size()) {
                for (int i = 0; i < timestamps.size(); i++) {
                    if (closePrices.get(i) == null) {
                        continue;
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date((Long.parseLong(timestamps.get(i).toString())) * 1000));
                    if (UnderlayingPeriod.D5.getValue().equalsIgnoreCase(underlayingPeriod)) {
                        //map.put(MONTH_NAMES[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.DAY_OF_MONTH) + "", closePrices.get(i).toString());
                        map.put(DAYS_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK)] + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE), closePrices.get(i).toString());
                    } else {
                        map.put(new Date((Long.parseLong(timestamps.get(i).toString())) * 1000), closePrices.get(i).toString());
                    }
                }
            }
            return map;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("Unexpected format of json {}", data);
            logger.error("Exception during parse data from yahoo service.", e);
        } catch (IOException e) {
            logger.error("Cannot read json to object", e);
        }
        return ArrayListMultimap.create();
    }

    @Override
    protected String getUrl(String underlayingPeriod) {
        if(UnderlayingPeriod.D5.getValue().equalsIgnoreCase(underlayingPeriod)) {
            return URL_5D;
        } else {
            return URL_YEAR;
        }
    }
}
