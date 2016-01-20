package com.structuredproducts.sevices;

import com.structuredproducts.controllers.data.InvestIdea;
import org.apache.log4j.Logger;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vlad on 10.01.2016.
 */
public class InvestIdeasService extends AbstractCSVService{

    private static final Logger log = Logger.getLogger(MailService.class);

    private static final String INVESTIDEAS_FILENAME = "data/invest_ideas.csv";

    private static final CellProcessor[] INVESTIDEAS_PROCESSORS = new CellProcessor[] {
            new ParseLong(),                //id
            new NotNull(),                  //company
            new ParseDate("dd.MM.yyyy"),    //Date
            new ParseBool(),                //show on main page
            new NotNull(),                  //header
            new NotNull(),                  //preview
            new NotNull(),                  //content
    };


    public List<InvestIdea> getInvestIdeas(){
        return (List<InvestIdea>) getListFromFile(InvestIdea.class, INVESTIDEAS_FILENAME, INVESTIDEAS_PROCESSORS);
    }

    public List<InvestIdea> getInvestIdeasForMainPage() {
        List<InvestIdea> list = (List<InvestIdea>) getListFromFile(InvestIdea.class, INVESTIDEAS_FILENAME, INVESTIDEAS_PROCESSORS);
        return list.stream().filter(v -> v.isShowOnMainPage()).collect(Collectors.toList());
    }
}
