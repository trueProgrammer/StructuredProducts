package com.structuredproducts.sevices;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Vlad on 24.03.2016.
 */
public class YahooUnderlayingPriceService {

    //private static final String URL = "http://ichart.yahoo.com/table.csv?s=MSFT&a=01&b=12&c=2007&d=10&e=1&f=2015&g=d&ignore=.csv";
    private static final String URL = "http://ichart.yahoo.com/table.csv?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d&g=d&ignore=.csv";

    public static Map<Date, Double> getHistoricalQuotes(String instrument, Date from, Date to) throws IOException {

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
            throw new RuntimeException("Client: status code is not OK : " + response.getStatusLine().getStatusCode());
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
            e.printStackTrace();
        }

        return result;
    }
}
