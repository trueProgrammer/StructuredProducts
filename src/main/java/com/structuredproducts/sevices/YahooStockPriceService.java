package com.structuredproducts.sevices;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseLong;
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
public class YahooStockPriceService extends HistoricalCachingDataService {

    private static final String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December" };

    private final static Logger logger = LoggerFactory.getLogger(YahooStockPriceService.class);

    private static final CellProcessor[] DAY_PROCESSORS = new CellProcessor[] {
            new ParseDate("yyyy-MM-dd"),    //Date
            new ParseDouble(),              //Open
            new ParseDouble(),              //High
            new ParseDouble(),              //Low
            new ParseDouble(),              //Close
            new ParseLong(),                 //Volume
            new ParseDouble(),              //Adj Close
    };
    // http://finance.yahoo.com/d/quotes.csv?e=.csv&f=c4l1&s=EURUSD=X,GBPUSD=X
    //private static final String URL = "http://ichart.yahoo.com/table.csv?s=MSFT&a=01&b=12&c=2007&d=10&e=1&f=2015&g=d&ignore=.csv";
    //private static final String URL = "http://ichart.yahoo.com/table.csv?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d&g=m&ignore=.csv";

    private static final String URL = "http://ichart.yahoo.com/table.csv?s=%s&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d&g=d&ignore=.csv";

    @Override
    protected String prepareUrl(String urlTemplate, String symbol, String type, Calendar startDate, Calendar endDate) {
        return String.format(URL, symbol,
                startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH), startDate.get(Calendar.YEAR),
                endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH), endDate.get(Calendar.YEAR));
    }

    @Override
    protected Multimap<?, String> parseData(InputStream inputStream, String underlayingPeriod) {
        //Map<String, String> result = new LinkedHashMap<>();
        Multimap<Date, String> result = LinkedListMultimap.create();
        /*Map<Date, Double> quotes = parseCsvQuotesByDayToMap(inputStream);
        for(Map.Entry<Date, Double> quote : quotes.entrySet()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(quote.getKey());
            result.put(MONTH_NAMES[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR), quote.getValue().toString());
        }*/
        return result;
    }

    @Override
    protected String getUrl(String underlayingPeriod) {
        return URL;
    }

    private Map<Date, Double> parseCsvQuotesByDayToMap(InputStream inputStream) {

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
