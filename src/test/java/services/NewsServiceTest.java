package services;

import com.structuredproducts.data.News;
import com.structuredproducts.sevices.NewsService;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Vlad on 26.12.2015.
 */
public class NewsServiceTest {

    private final NewsService newsService = new NewsService();

    @Test
    public void parseNewsTest() {
        List<News> news = newsService.getNews();

        assertThat(news, Matchers.notNullValue());
        assertThat(news.size(), is(2));
    }

    @Test
    public void getNewsByDateAndHeaderTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 11, 26);
        Date date = calendar.getTime();
        String header = "New site!";

        News news = newsService.getNewsByDateAndDate(header, date);

        assertThat(news, Matchers.notNullValue());
    }

}
