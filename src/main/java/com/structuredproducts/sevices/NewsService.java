package com.structuredproducts.sevices;

import com.structuredproducts.controllers.data.News;
import org.apache.log4j.Logger;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

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
