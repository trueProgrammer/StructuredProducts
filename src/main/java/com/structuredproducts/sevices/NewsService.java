package com.structuredproducts.sevices;

import com.structuredproducts.data.News;
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
public class NewsService extends AbstractCSVService{

    private static final Logger log = Logger.getLogger(MailService.class);

    private static final String NEWS_FILENAME = "data/news.csv";

    private static final CellProcessor[] NEWS_PROCESSORS = new CellProcessor[] {
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
        return (List<News>) getListFromFile(News.class, NEWS_FILENAME, NEWS_PROCESSORS);
    }

}
