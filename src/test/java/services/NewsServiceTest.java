package services;

import com.structuredproducts.rest.News;
import com.structuredproducts.sevices.NewsService;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Vlad on 26.12.2015.
 */
public class NewsServiceTest {

    @Test
    public void parseNewsTest() {
        NewsService newsService = new NewsService();

        List<News> news = newsService.getNews();

        assertThat(news, Matchers.notNullValue());
        assertThat(news.size(), is(2));
    }

    @Test
    public void getNewsByDateAndHeaderTest() {
        NewsService newsService = new NewsService();


        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 11, 26);
        Date date = calendar.getTime();
        String header = "New site!";

        News news = newsService.getNewsByDateAndDate(header, date);

        assertThat(news, Matchers.notNullValue());
    }

}
