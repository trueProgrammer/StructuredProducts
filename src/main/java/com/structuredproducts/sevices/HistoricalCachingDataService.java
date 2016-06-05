package com.structuredproducts.sevices;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.structuredproducts.persistence.entities.instrument.Underlaying;
import com.structuredproducts.persistence.entities.instrument.UnderlayingPeriod;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public abstract class HistoricalCachingDataService {
    private final static Logger logger = LoggerFactory.getLogger(HistoricalCachingDataService.class);
    private static final int CACHE_SIZE = 35;
    private static final String UTF8 = "UTF-8";

    protected final LoadingCache<Underlaying, Multimap<?, String>> cache = CacheBuilder
        .newBuilder()
        .maximumSize(CACHE_SIZE)
        .expireAfterAccess(24, TimeUnit.HOURS)
        .build(new CacheLoader<Underlaying, Multimap<?, String>>() {
            @Override
            public Multimap<?, String> load(Underlaying underlaying) throws Exception {
                return loadData(underlaying);
            }
        });

    public Multimap<?, String> getHistoricalCachingData(Underlaying underlaying) throws ExecutionException {
        return cache.get(underlaying);
    }

    public void invalidateCache() {
        cache.invalidateAll();
    }

    protected Multimap<?, String> loadData(Underlaying underlaying) throws IOException {
        String symbol = underlaying.getOfficialName();
        String url = prepareUrl(getUrl(underlaying.getPeriod()), symbol, underlaying.getType() != null ? underlaying.getType().getName() : null, underlaying.getPeriod());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);

        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            logger.error("Client: status code is not OK : {} instrument: {} URL: {}", response.getStatusLine().getStatusCode(), symbol, url);
            return ArrayListMultimap.create();
        }

        return parseData(response.getEntity().getContent(), underlaying.getPeriod());
    }
    private String prepareUrl(String urlTemplate, String symbol, String type, String underlayingPeriod) throws UnsupportedEncodingException {
        Calendar endDate = new GregorianCalendar();
        Calendar startDate = new GregorianCalendar();
        if(UnderlayingPeriod.D5.getValue().equalsIgnoreCase(underlayingPeriod)) {
            startDate.set(Calendar.DAY_OF_MONTH, endDate.get(Calendar.DAY_OF_MONTH) - 5);
        } else {
            //default take 1 year period date
            startDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR) - 1);
            startDate.set(Calendar.MONTH, endDate.get(Calendar.MONTH) + 1);
        }
        return prepareUrl(urlTemplate, URLEncoder.encode(symbol, UTF8), type, startDate, endDate);
    }

    protected abstract String prepareUrl(String urlTemplate, String symbol, String type, Calendar startDate, Calendar endDate);
    protected abstract Multimap<?, String> parseData(InputStream data, String underlayingPeriod) throws IOException;
    protected abstract String getUrl(String underlayingPeriod);
}