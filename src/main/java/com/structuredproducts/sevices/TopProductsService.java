package com.structuredproducts.sevices;

import com.structuredproducts.data.*;
import org.apache.log4j.Logger;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Vlad on 26.12.2015.
 */
public class TopProductsService extends AbstractCSVService {

    private static final Logger log = Logger.getLogger(TopProductsService.class);

    private static final String TOP_PRODUCTS_FILENAME = "data/top_products.csv";

    private static final CellProcessor[] TOP_PRODUCTS_PROCESSORS = new CellProcessor[] {
            new ParseLong(),                        //id
            new NotNull(),                          //name
            new NotNull(),                          //bank
            new ParseEnum(Currency.class, true),    //currency
            new ParseInt(),                         //invest
            new ParseDouble(),                      //date
            new ParseInt(),                         //participation
            new ParseEnum(TimeType.class, true),    //time type
            new ParseEnum(ProductType.class, true), //product type
    };

    public List<TopProduct> getTopProductsByTimeTypeAndProductType(final TimeType timeType, final ProductType productType) {
        List<TopProduct> products = getTopProducts();

        if(productType != ProductType.All) {
            products = products.stream()
                    .filter(v -> v.getTimeType() == timeType && v.getProductType() == productType)
                    .collect(Collectors.toList());
        } else {
            products = products.stream()
                    .filter(v -> v.getTimeType() == timeType)
                    .collect(Collectors.toList());

            final Random random = new Random();
            //show only five products
            for(int i = products.size();products.size() > 5; i--) {
                products.remove(random.nextInt(i));
            }
        }

        return products;
    }

    public List<TopProduct> getTopProducts(){
        return (List<TopProduct>) getListFromFile(TopProduct.class, TOP_PRODUCTS_FILENAME, TOP_PRODUCTS_PROCESSORS);
    }

}
