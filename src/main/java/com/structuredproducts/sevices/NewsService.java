package com.structuredproducts.sevices;

import com.structuredproducts.rest.News;
import com.structuredproducts.util.ServiceUtils;
import org.apache.log4j.Logger;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vlad on 26.12.2015.
 */
public class NewsService {

    private static final Logger log = Logger.getLogger(MailService.class);

    private static final String NEWS_FILENAME = "data/news.csv";

    private static final CellProcessor[] DAY_PROCESSORS = new CellProcessor[] {
            new ParseDate("dd.MM.yyyy"),    //Date
            new NotNull(),                  //header
            new NotNull(),                  //preview
            new NotNull(),                  //content
    };

    public News getNewsByDateAndDate(String header, Date date) {

        List<News> list = getNews();

        for(News  news: list) {
            if(ServiceUtils.compareDatesWithoutTimes(news.getDate(), date) && news.getHeader().equals(header)) {
                return news;
            }
        }

        return null;
    }



    public List<News> getNews(){
        ICsvBeanReader beanReader = null;
        List<News> list = new ArrayList<>();
        try {
            String file = this.getClass().getClassLoader().getResource(NEWS_FILENAME).getFile();
            beanReader = new CsvBeanReader(new FileReader(file), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            final String[] header = beanReader.getHeader(true);

            News news;
            while( (news = beanReader.read(News.class, header, DAY_PROCESSORS)) != null ) {
                list.add(news);
            }

        } catch (FileNotFoundException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        } finally {
            if( beanReader != null ) {
                try {
                    beanReader.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }

        return list;
    }

}
