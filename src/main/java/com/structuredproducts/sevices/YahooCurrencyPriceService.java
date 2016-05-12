package com.structuredproducts.sevices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class YahooCurrencyPriceService implements ChartDataService{
    private final static Logger logger = LoggerFactory.getLogger(YahooCurrencyPriceService.class);
    private static final int CACHE_SIZE = 35;

    private static final String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December" };
    private final static String URL = "https://finance-yql.media.yahoo.com/v7/finance/chart/%s=X?period1=%d&period2=%d&interval=1mo&indicators=quote&includeTimestamps=true&includePrePost=true&events=div%%7Csplit%%7Cearn&corsDomain=finance.yahoo.com";

    private final LoadingCache<String, Map<String, String>> cache = CacheBuilder
            .newBuilder()
            .maximumSize(CACHE_SIZE)
            .expireAfterAccess(24, TimeUnit.HOURS)
            .build(new CacheLoader<String, Map<String, String>>() {
                @Override
                public Map<String, String> load(String baseActive) throws Exception {
                    return loadData(baseActive);
                }
            });

    @Override
    public Map<String, String> getChartData(String symbol) throws ExecutionException {
        return cache.get(symbol);
    }

    Map<String, String> loadData(String symbol) {
        String url = prepareUrl(URL, symbol);
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(get);
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), stringWriter);
            return parseData(stringWriter.toString());
        } catch (IOException e) {
            return new LinkedHashMap<>();
        }
    }

    private String prepareUrl(String urlTemplate, String symbol) {
        Calendar endDate = new GregorianCalendar();
        Calendar startDate = new GregorianCalendar();
        startDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) - 1);
        return String.format(urlTemplate, symbol, startDate.getTimeInMillis() / 1000, endDate.getTimeInMillis() / 1000);
    }

    private Map<String, String> parseData(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object jsonObj = mapper.readValue(data, Object.class);
            Object result = ((List) PropertyUtils.getProperty(jsonObj, "chart.result")).get(0);
            Object quote = ((List) PropertyUtils.getProperty(result, "indicators.quote")).get(0);

            List timestamps = ((List) PropertyUtils.getProperty(result, "timestamp"));
            List closePrices = ((List) PropertyUtils.getProperty(quote, "close"));

            Map<String, String> map = new LinkedHashMap<>();
            if (timestamps.size() == closePrices.size()) {
                for (int i = 0; i < timestamps.size(); i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date((Long.parseLong(timestamps.get(i).toString())) * 1000));
                    map.putIfAbsent(MONTH_NAMES[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR), closePrices.get(i).toString());
                }
            }
            return map;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("Unexpected format of json {}", data);
            logger.error("", e);
        } catch (IOException e) {
            logger.error("Cannot read json to object", e);
        }

        return new LinkedHashMap<>();
    }
}
