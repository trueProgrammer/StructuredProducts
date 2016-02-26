package com.structuredproducts.sevices;

import com.structuredproducts.controllers.data.ProductBean;
import com.structuredproducts.persistence.entities.instrument.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductCsvToDbService {
    @Autowired
    DBService dbService;

    private Map<String, String> beanPropertiesToColumnName = new HashMap<>();

    {
        beanPropertiesToColumnName.put("name", "\u041D\u0430\u0437\u0432\u0430\u043D\u0438\u0435");//Название
        beanPropertiesToColumnName.put("description", "\u041E\u043F\u0438\u0441\u0430\u043D\u0438\u0435");//Описание
        beanPropertiesToColumnName.put("productType", "\u0422\u0438\u043F \u043F\u0440\u043E\u0434\u0443\u043A\u0442\u0430");//Тип продукта
        beanPropertiesToColumnName.put("term","\u0421\u0440\u043E\u043A");//Срок
        beanPropertiesToColumnName.put("underlying", "\u0411\u0430\u0437\u043E\u0432\u044B\u0439 \u0430\u043A\u0442\u0438\u0432"); //"Базовый актив"
        beanPropertiesToColumnName.put("minInvestment","\u041C\u0438\u043D\u0438\u043C\u0430\u043B\u044C\u043D\u0430\u044F \u0441\u0443\u043C\u043C\u0430");// "Минимальная сумма");
        beanPropertiesToColumnName.put("maxInvestment","\u041C\u0430\u043A\u0441\u0438\u043C\u0430\u043B\u044C\u043D\u0430\u044F \u0441\u0443\u043C\u043C\u0430");// "Максимальная сумма");
        beanPropertiesToColumnName.put("issuer","\u041F\u0440\u043E\u0432\u0430\u0439\u0434\u0435\u0440");// "Провайдер");
        beanPropertiesToColumnName.put("return","\u0414\u043E\u0445\u043E\u0434\u043D\u043E\u0441\u0442\u044C");// "Доходность");
        beanPropertiesToColumnName.put("strategy", "\u0421\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u044F");// "Стратегия");
        beanPropertiesToColumnName.put("legalType", "\u042E\u0440\u0438\u0434\u0438\u0447\u0435\u0441\u043A\u0430\u044F \u0444\u043E\u0440\u043C\u0430");//"Юридическая форма");
        beanPropertiesToColumnName.put("payoff","\u0420\u0430\u0437\u043C\u0435\u0440 \u0432\u044B\u043F\u043B\u0430\u0442\u044B");// "Размер выплаты");
        beanPropertiesToColumnName.put("risks", "\u0420\u0438\u0441\u043A\u0438");//"Риски");
        beanPropertiesToColumnName.put("currency", "\u0412\u0430\u043B\u044E\u0442\u0430");//"Валюта");
        beanPropertiesToColumnName.put("periodicity", "\u041F\u0435\u0440\u0438\u043E\u0434\u0438\u0447\u043D\u043E\u0441\u0442\u044C \u0432\u044B\u043F\u043B\u0430\u0442");//"Периодичность выплат");
    }

    public void convert(InputStreamReader reader) {
        List<ProductBean> productList = null;
        try {
            productList = readCsv(reader);
            List<Product> convertedProducts = productList.stream().<Product>map(productBean -> {
                Product product = new Product();
                product.setName(productBean.getName());
                product.setDescription(productBean.getDescription());
                product.setCurrency(new Currency(productBean.getCurrency()));
                product.setUnderlaying(new Underlaying(productBean.getUnderlying()));
                product.setInvestment(new Investment(productBean.getMinInvestment(), productBean.getMaxInvestment()));
                product.setIssuer(new Issuer(productBean.getIssuer()));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<ProductBean> readCsv(InputStreamReader reader) throws IOException {
        List<ProductBean> result = new ArrayList<>();
        Map<String, Object> productsMap;

        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(reader, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            String[] header = mapReader.getHeader(true);
            while ((productsMap = mapReader.read(header, ProductService.PRODUCTS_PROCESSORS)) != null) {
                ProductBean bean = new ProductBean();
                bean.setName((String) productsMap.get(beanPropertiesToColumnName.get("name")));
                bean.setDescription((String) productsMap.get(beanPropertiesToColumnName.get("description")));
                bean.setProductType((String) productsMap.get(beanPropertiesToColumnName.get("productType")));
                bean.setTerm((Integer)productsMap.get(beanPropertiesToColumnName.get("term")));
                bean.setUnderlying((String) productsMap.get(beanPropertiesToColumnName.get("underlying")));
                bean.setMinInvestment((Integer) productsMap.get(beanPropertiesToColumnName.get("minInvestment")));
                bean.setMaxInvestment((Integer) productsMap.get(beanPropertiesToColumnName.get("maxInvestment")));
                bean.setIssuer((String) productsMap.get(beanPropertiesToColumnName.get("issuer")));
                bean.setReturn((Integer) productsMap.get(beanPropertiesToColumnName.get("return")));
                bean.setStrategy((String) productsMap.get(beanPropertiesToColumnName.get("strategy")));
                bean.setLegalType((String) productsMap.get(beanPropertiesToColumnName.get("legalType")));
                bean.setPayoff((String) productsMap.get(beanPropertiesToColumnName.get("payoff")));
                bean.setRisk((String) productsMap.get(beanPropertiesToColumnName.get("risks")));
                bean.setCurrency((String) productsMap.get(beanPropertiesToColumnName.get("currency")));
                bean.setPeriodicity((String) productsMap.get(beanPropertiesToColumnName.get("periodicity")));
                result.add(bean);

            }

        } finally {
            if (mapReader != null) {
                mapReader.close();
            }
        }
        return result;
    }
}
