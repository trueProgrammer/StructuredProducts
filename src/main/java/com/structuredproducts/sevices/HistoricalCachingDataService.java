package com.structuredproducts.sevices;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public abstract class HistoricalCachingDataService {
    private final static Logger logger = LoggerFactory.getLogger(HistoricalCachingDataService.class);
    private static final int CACHE_SIZE = 35;

    protected final LoadingCache<String, Map<String, String>> cache = CacheBuilder
        .newBuilder()
        .maximumSize(CACHE_SIZE)
        .expireAfterAccess(24, TimeUnit.HOURS)
        .build(new CacheLoader<String, Map<String, String>>() {
            @Override
            public Map<String, String> load(String baseActive) throws Exception {
                return loadData(baseActive);
            }
        });

    public Map<String, String> getHistoricalCachingData(String symbol) throws ExecutionException {
        return cache.get(symbol);
    }

    protected Map<String, String> loadData(String symbol) throws IOException {
        String url = prepareUrl(getUrl(), symbol);
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);

        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            logger.error("Client: status code is not OK : {} instrument: {} URL: {}", response.getStatusLine().getStatusCode(), symbol, url);
            return new LinkedHashMap<>();
        }

        return parseData(response.getEntity().getContent());
    }
    private String prepareUrl(String urlTemplate, String symbol) {
        Calendar endDate = new GregorianCalendar();
        Calendar startDate = new GregorianCalendar();
        startDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) - 1);
        return prepareUrl(urlTemplate, symbol, startDate, endDate);
    }

    protected abstract String prepareUrl(String urlTemplate, String symbol, Calendar startDate, Calendar endDate);
    protected abstract Map<String, String> parseData(InputStream data) throws IOException;
    protected abstract String getUrl();
}
