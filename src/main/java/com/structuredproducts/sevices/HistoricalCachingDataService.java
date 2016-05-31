package com.structuredproducts.sevices;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.structuredproducts.controllers.data.Tuple;
import com.structuredproducts.persistence.entities.instrument.UnderlayingType;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Holder;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public abstract class HistoricalCachingDataService {
    private final static Logger logger = LoggerFactory.getLogger(HistoricalCachingDataService.class);
    private static final int CACHE_SIZE = 35;
    private static final String UTF8 = "UTF-8";

    protected final LoadingCache<Tuple, Multimap<Date, String>> cache = CacheBuilder
        .newBuilder()
        .maximumSize(CACHE_SIZE)
        .expireAfterAccess(24, TimeUnit.HOURS)
        .build(new CacheLoader<Tuple, Multimap<Date, String>>() {
            @Override
            public Multimap<Date, String> load(Tuple tuple) throws Exception {
                return loadData(tuple.getName(), tuple.getValue());
            }
        });

    public Multimap<Date, String> getHistoricalCachingData(String symbol, UnderlayingType underlayingType) throws ExecutionException {
        return cache.get(new Tuple(symbol, underlayingType != null ? underlayingType.getName() : null));
    }

    public void invalidateCache() {
        cache.invalidateAll();
    }

    protected Multimap<Date, String> loadData(String symbol, String type) throws IOException {
        String url = prepareUrl(getUrl(), symbol, type);
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);

        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            logger.error("Client: status code is not OK : {} instrument: {} URL: {}", response.getStatusLine().getStatusCode(), symbol, url);
            return ArrayListMultimap.create();
        }

        return parseData(response.getEntity().getContent());
    }
    private String prepareUrl(String urlTemplate, String symbol, String type) throws UnsupportedEncodingException {
        Calendar endDate = new GregorianCalendar();
        Calendar startDate = new GregorianCalendar();
        startDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) - 1);
        startDate.set(Calendar.MONTH, endDate.get(Calendar.MONTH) + 1);
        return prepareUrl(urlTemplate, URLEncoder.encode(symbol, UTF8), type, startDate, endDate);
    }

    protected abstract String prepareUrl(String urlTemplate, String symbol, String type, Calendar startDate, Calendar endDate);
    protected abstract Multimap<Date, String> parseData(InputStream data) throws IOException;
    protected abstract String getUrl();
}