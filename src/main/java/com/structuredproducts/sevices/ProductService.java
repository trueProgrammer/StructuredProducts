package com.structuredproducts.sevices;

import com.structuredproducts.controllers.data.Product;
import org.apache.log4j.Logger;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.util.List;

public class ProductService extends AbstractCSVService {
    private static final Logger logger = Logger.getLogger(ProductService.class);

    private static final String PRODUCTS_FILENAME = "data/products.csv";

    private final static CellProcessor[] PRODUCTS_PROCESSORS = new CellProcessor[]{
            new ParseLong(), //id
            new NotNull(),   //name
            new ParseInt(),  //term
            new NotNull(),   //base active/underlying
            new ParseInt(),  //min investment
            new NotNull(),   //provider/issuer
            new ParseInt(),  //profit/return
            new NotNull(),   //strategy
            new NotNull(),   //legal type
            new ParseInt(),  //payoff
            new NotNull(),   //risks
            new NotNull(),   //currency
            new NotNull(),   //periodicity
    };

    public List<Product> getProducts() {
        return getListFromFile(Product.class, PRODUCTS_FILENAME, PRODUCTS_PROCESSORS);
    }
}
