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
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vlad on 24.03.2016.
 */
public class YahooUnderlayingPriceService {

    private static final int CACHE_SIZE = 35;

    private static final String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December" };

    private final static Logger logger = LoggerFactory.getLogger(YahooUnderlayingPriceService.class);

    // http://finance.yahoo.com/d/quotes.csv?e=.csv&f=c4l1&s=EURUSD=X,GBPUSD=X
    //private static final String URL = "http://ichart.yahoo.com/table.csv?s=MSFT&a=01&b=12&c=2007&d=10&e=1&f=2015&g=d&ignore=.csv";
    //private static final String URL = "http://ichart.yahoo.com/table.csv?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d&g=m&ignore=.csv";

    public static final LoadingCache<String, Map<String, String>> cache = CacheBuilder
            .newBuilder()
            .maximumSize(CACHE_SIZE)
            .expireAfterAccess(24, TimeUnit.HOURS)
            .build(new CacheLoader<String, Map<String, String>>() {
                @Override
                public Map<String, String> load(String baseActive) throws Exception {
                    return getYearHistoricalQuotes(baseActive);
                }
            });

    public static Map<String, String> getYearHistoricalQuotes(String product) throws IOException {
        Date date = new Date();
        Calendar to = Calendar.getInstance();
        to.setTime(date);
        Calendar from = Calendar.getInstance();
        from.setTime(date);
        from.set(Calendar.YEAR, to.get(Calendar.YEAR) - 1);
        String URL = "http://ichart.yahoo.com/table.csv?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d&g=m&ignore=.csv";
        Map<String, String> result = new LinkedHashMap<>();
        Map<Date, Double> quotes = getHistoricalQuotes(product, from.getTime(), to.getTime(), URL);
        for(Map.Entry<Date, Double> quote : quotes.entrySet()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(quote.getKey());
            result.putIfAbsent(MONTH_NAMES[calendar.get(Calendar.MONTH)], quote.getValue().toString());
        }
        return result;
    }

    public static Map<Date, Double> getHistoricalQuotes(String instrument, Date from, Date to, String URL) throws IOException {

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(from);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(to);

        final String url = String.format(URL, instrument,
                fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH), fromCalendar.get(Calendar.YEAR),
                toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH), toCalendar.get(Calendar.YEAR));

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);

        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            throw new RuntimeException("Client: status code is not OK : " + response.getStatusLine().getStatusCode()
            + " .Instrumen=" + instrument + " URL=" + url);
        }

        return parseCsvQuotesByDayToMap(response.getEntity().getContent());
    }

    private static final CellProcessor[] DAY_PROCESSORS = new CellProcessor[] {
            new ParseDate("yyyy-MM-dd"),    //Date
            new ParseDouble(),              //Open
            new ParseDouble(),              //High
            new ParseDouble(),              //Low
            new ParseDouble(),              //Close
            new ParseInt(),                 //Volume
            new ParseDouble(),              //Adj Close
    };

    public static Map<Date, Double> parseCsvQuotesByDayToMap(InputStream inputStream) {

        final Map<Date, Double> result = new LinkedHashMap<>();

        try(ICsvMapReader mapReader = new CsvMapReader(new InputStreamReader(inputStream), CsvPreference.STANDARD_PREFERENCE)) {
            final String[] headers = mapReader.getHeader(true);

            Map<String, Object> map;
            while( (map = mapReader.read(headers, DAY_PROCESSORS)) != null) {
                result.put((Date) map.get("Date"), (Double) map.get("Close"));
            }
        } catch (IOException e) {
           logger.error("Error while parse quotes from yahoo service.", e);
        }

        return result;
    }
}
