package com.structuredproducts.sevices;

import com.structuredproducts.controllers.data.ProductBean;
import com.structuredproducts.persistence.entities.instrument.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractCsvToDbService {
    @Autowired
    ProductService productService;
    @Autowired
    DBService dbService;

    public void convert(InputStreamReader reader) {
        List<ProductBean> productList = productService.getProducts(reader);
        List<Product> convertedProducts = productList.stream().<Product>map(productBean -> {
            Product product = new Product();
            product.setCurrency(new Currency(productBean.getCurrency()));
            Underlaying underlaying = new Underlaying();
            underlaying.setName(productBean.getBaseActive());
            product.setUnderlaying(underlaying);
            product.setInvestment(new Investment(productBean.getMinInvestment(), productBean.getMinInvestment()));
            product.setIssuer(new Issuer(productBean.getProvider()));
            product.setReturnValue(new Return(productBean.getProfit()));
            product.setStrategy(new Strategy(productBean.getStrategy()));
            product.setLegalType(new LegalType(productBean.getLegalType()));
            product.setPayoff(new PayOff(productBean.getPayoff()));
            product.setRisks(new Risks(productBean.getRisk()));
            product.setTerm(new Term(productBean.getTerm(), productBean.getTerm()));
            product.setProductType(new ProductType(productBean.getProductType()));
            product.setPaymentPeriodicity(new PaymentPeriodicity(productBean.getPeriodicity()));
            return product;
        }).collect(Collectors.toList());
        dbService.saveProducts(convertedProducts);
    }
}
