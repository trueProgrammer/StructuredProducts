package services;

import com.structuredproducts.controllers.data.InvestIdeaBean;
import com.structuredproducts.sevices.InvestIdeasService;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by Vlad on 10.01.2016.
 */
public class InvestIdeasServiceTest {

    private final InvestIdeasService investIdeasService = new InvestIdeasService();

    @Test
    public void test() {
        List<InvestIdeaBean> list = investIdeasService.getInvestIdeas();
        Assert.assertTrue(list.size() == 2);

        list = investIdeasService.getInvestIdeasForMainPage();
        Assert.assertTrue(list.size() == 1);
    }

}
