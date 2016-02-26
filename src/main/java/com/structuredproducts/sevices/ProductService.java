package com.structuredproducts.sevices;

import com.structuredproducts.controllers.data.ProductBean;
import org.apache.log4j.Logger;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService extends AbstractCSVService {
    private static final Logger logger = Logger.getLogger(ProductService.class);

    private static final String PRODUCTS_FILENAME = "data/products.csv";

    public final static CellProcessor[] PRODUCTS_PROCESSORS = new CellProcessor[]{
            new NotNull(),   //name
            new NotNull(),   //description
            new NotNull(),   //product type
            new ParseInt(),  //term
            new NotNull(),   //base active/underlying
            new ParseInt(),  //min investment
            new ParseInt(),  //max investment
            new NotNull(),   //provider/issuer
            new ParseInt(),  //profit/return
            new NotNull(),   //strategy
            new NotNull(),   //legal type
            new NotNull(),  //payoff
            new NotNull(),   //risks
            new NotNull(),   //currency
            new NotNull(),   //periodicity
    };

    public List<ProductBean> getProductsByRisks(String[] risks) {
        List list = new ArrayList<>();
        Collections.addAll(list, risks);
        HashSet<String> risksSet = new HashSet<String>(list);
        return getProducts().stream().filter(product -> risksSet.contains(product.getRisk())).collect(Collectors.toList());
    }
    public List<ProductBean> getProducts() {
        return getListFromFile(ProductBean.class, PRODUCTS_FILENAME, PRODUCTS_PROCESSORS);
    }

    public List<ProductBean> getProducts(InputStreamReader reader) {
        return getListFromStream(ProductBean.class, reader, PRODUCTS_PROCESSORS);
    }
}
