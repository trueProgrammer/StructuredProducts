package com.structuredproducts.sevices;

import com.structuredproducts.controllers.data.ProductBean;
import com.structuredproducts.persistence.entities.instrument.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductCsvToDbService {
    public final static CellProcessor[] PRODUCTS_PROCESSORS = new CellProcessor[]{
                                                                                         new NotNull(),   //name
                                                                                         new NotNull(),   //product type
                                                                                         new ParseInt(),  //minTerm
                                                                                         new ParseInt(),  //maxTerm
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
    @Autowired
    DBService dbService;

    private Map<String, String> beanPropertiesToColumnName = new HashMap<>();
    {
        beanPropertiesToColumnName.put("name", "Название");//��������
        beanPropertiesToColumnName.put("productType", "\u0422\u0438\u043F \u043F\u0440\u043E\u0434\u0443\u043A\u0442\u0430");//��� ��������
        beanPropertiesToColumnName.put("minTerm","\u041C\u0438\u043D\u0438\u043C\u0430\u043B\u044C\u043D\u044B\u0439 \u0441\u0440\u043E\u043A");//����
        beanPropertiesToColumnName.put("maxTerm", "\u041C\u0430\u043A\u0441\u0438\u043C\u0430\u043B\u044C\u043D\u044B\u0439 \u0441\u0440\u043E\u043A");//����
        beanPropertiesToColumnName.put("underlying", "\u0411\u0430\u0437\u043E\u0432\u044B\u0439 \u0430\u043A\u0442\u0438\u0432"); //"������� �����"
        beanPropertiesToColumnName.put("minInvestment","\u041C\u0438\u043D\u0438\u043C\u0430\u043B\u044C\u043D\u0430\u044F \u0441\u0443\u043C\u043C\u0430");// "����������� �����");
        beanPropertiesToColumnName.put("maxInvestment","\u041C\u0430\u043A\u0441\u0438\u043C\u0430\u043B\u044C\u043D\u0430\u044F \u0441\u0443\u043C\u043C\u0430");// "������������ �����");
        beanPropertiesToColumnName.put("broker","\u041F\u0440\u043E\u0432\u0430\u0439\u0434\u0435\u0440");// "���������");
        beanPropertiesToColumnName.put("return","\u0414\u043E\u0445\u043E\u0434\u043D\u043E\u0441\u0442\u044C");// "����������");
        beanPropertiesToColumnName.put("strategy", "\u0421\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u044F");// "���������");
        beanPropertiesToColumnName.put("legalType", "\u042E\u0440\u0438\u0434\u0438\u0447\u0435\u0441\u043A\u0430\u044F \u0444\u043E\u0440\u043C\u0430");//"����������� �����");
        beanPropertiesToColumnName.put("payoff","\u0420\u0430\u0437\u043C\u0435\u0440 \u0432\u044B\u043F\u043B\u0430\u0442\u044B");// "������ �������");
        beanPropertiesToColumnName.put("risks", "\u0420\u0438\u0441\u043A\u0438");//"�����");
        beanPropertiesToColumnName.put("currency", "\u0412\u0430\u043B\u044E\u0442\u0430");//"������");
        beanPropertiesToColumnName.put("periodicity", "\u041F\u0435\u0440\u0438\u043E\u0434\u0438\u0447\u043D\u043E\u0441\u0442\u044C \u0432\u044B\u043F\u043B\u0430\u0442");//"������������� ������");
    }

    private String header[] = new String[]{
            "Название",
            "\u0422\u0438\u043F \u043F\u0440\u043E\u0434\u0443\u043A\u0442\u0430",//��� ��������
            "\u041C\u0438\u043D\u0438\u043C\u0430\u043B\u044C\u043D\u044B\u0439 \u0441\u0440\u043E\u043A",//����
            "\u041C\u0430\u043A\u0441\u0438\u043C\u0430\u043B\u044C\u043D\u044B\u0439 \u0441\u0440\u043E\u043A",
            "\u0411\u0430\u0437\u043E\u0432\u044B\u0439 \u0430\u043A\u0442\u0438\u0432", //"������� �����"
            "\u041C\u0438\u043D\u0438\u043C\u0430\u043B\u044C\u043D\u0430\u044F \u0441\u0443\u043C\u043C\u0430",// "����������� �����",
            "\u041C\u0430\u043A\u0441\u0438\u043C\u0430\u043B\u044C\u043D\u0430\u044F \u0441\u0443\u043C\u043C\u0430",// "������������ �����",
            "\u041F\u0440\u043E\u0432\u0430\u0439\u0434\u0435\u0440",// "���������",
            "\u0414\u043E\u0445\u043E\u0434\u043D\u043E\u0441\u0442\u044C",// "����������",
            "\u0421\u0442\u0440\u0430\u0442\u0435\u0433\u0438\u044F",// "���������",
            "\u042E\u0440\u0438\u0434\u0438\u0447\u0435\u0441\u043A\u0430\u044F \u0444\u043E\u0440\u043C\u0430",//"����������� �����",
            "\u0420\u0430\u0437\u043C\u0435\u0440 \u0432\u044B\u043F\u043B\u0430\u0442\u044B",// "������ �������",
            "\u0420\u0438\u0441\u043A\u0438",//"�����",
            "\u0412\u0430\u043B\u044E\u0442\u0430",//"������",
            "\u041F\u0435\u0440\u0438\u043E\u0434\u0438\u0447\u043D\u043E\u0441\u0442\u044C \u0432\u044B\u043F\u043B\u0430\u0442"//"������������� ������",
    };

    public void convertToDb(InputStreamReader reader) {
        List<ProductBean> productList = null;
        try {
            productList = readCsv(reader);
            List<Product> convertedProducts = productList.stream().<Product>map(productBean -> {
                Product product = new Product();
                product.setName(productBean.getName());
                product.setCurrency(new Currency(productBean.getCurrency()));
                product.setUnderlaying(new Underlaying(productBean.getUnderlying()));
                product.setInvestment(new Investment(productBean.getMinInvestment(), productBean.getMaxInvestment()));
                product.setBroker(new Broker(productBean.getBroker()));
                product.setReturnValue(new Return(productBean.getProfit()));
                product.setStrategy(new Strategy(productBean.getStrategy()));
                product.setLegalType(new LegalType(productBean.getLegalType()));
                product.setPayoff(new PayOff(productBean.getPayoff()));
                product.setRisks(new Risks(productBean.getRisk()));
                product.setTerm(new Term(productBean.getMinTerm(), productBean.getMaxTerm()));
                product.setProductType(new ProductType(productBean.getProductType()));
                product.setPaymentPeriodicity(new PaymentPeriodicity(productBean.getPeriodicity()));
                return product;
            }).collect(Collectors.toList());
            dbService.saveProducts(convertedProducts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String convertToCsv() throws IOException {
        List<Product> products = (List<Product>) dbService.getResultList(Product.class);

//        String[] header = beanPropertiesToColumnName.values().toArray(new String[beanPropertiesToColumnName.size()]);
        StringWriter stringWriter = new StringWriter();
        CsvMapWriter writer = new CsvMapWriter(stringWriter, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
        Map<String, Object> map = new HashMap<>();
        writer.writeHeader(header);

        try {
            for (Product product : products) {
                map.put(beanPropertiesToColumnName.get("name"), product.getName());
                map.put(beanPropertiesToColumnName.get("productType"), product.getProductType().getName());
                map.put(beanPropertiesToColumnName.get("minTerm"), product.getTerm().getMin());
                map.put(beanPropertiesToColumnName.get("maxTerm"), product.getTerm().getMax());
                map.put(beanPropertiesToColumnName.get("underlying"), product.getUnderlaying().getName());
                map.put(beanPropertiesToColumnName.get("minInvestment"), product.getInvestment().getMin());
                map.put(beanPropertiesToColumnName.get("maxInvestment"), product.getInvestment().getMax());
                map.put(beanPropertiesToColumnName.get("broker"), product.getBroker().getName());
                map.put(beanPropertiesToColumnName.get("return"), product.getReturnValue().getCount());
                map.put(beanPropertiesToColumnName.get("strategy"), product.getStrategy().getName());
                map.put(beanPropertiesToColumnName.get("legalType"), product.getLegalType().getName());
                map.put(beanPropertiesToColumnName.get("payoff"), product.getPayoff().getName());
                map.put(beanPropertiesToColumnName.get("risks"), product.getRisks().getName());
                map.put(beanPropertiesToColumnName.get("currency"), product.getCurrency().getName());
                map.put(beanPropertiesToColumnName.get("periodicity"), product.getPaymentPeriodicity().getName());
                writer.write(map, header, PRODUCTS_PROCESSORS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        return stringWriter.toString();
    }

    private List<ProductBean> readCsv(InputStreamReader reader) throws IOException {
        List<ProductBean> result = new ArrayList<>();
        Map<String, Object> productsMap;

        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(reader, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            String[] header = mapReader.getHeader(true);
            while ((productsMap = mapReader.read(header, PRODUCTS_PROCESSORS)) != null) {
                ProductBean bean = new ProductBean();
                bean.setName((String) productsMap.get(beanPropertiesToColumnName.get("name")));
                bean.setProductType((String) productsMap.get(beanPropertiesToColumnName.get("productType")));
                bean.setMinTerm((Integer)productsMap.get(beanPropertiesToColumnName.get("minTerm")));
                bean.setMaxTerm((Integer)productsMap.get(beanPropertiesToColumnName.get("maxTerm")));
                bean.setUnderlying((String) productsMap.get(beanPropertiesToColumnName.get("underlying")));
                bean.setMinInvestment((Integer) productsMap.get(beanPropertiesToColumnName.get("minInvestment")));
                bean.setMaxInvestment((Integer) productsMap.get(beanPropertiesToColumnName.get("maxInvestment")));
                bean.setBroker((String) productsMap.get(beanPropertiesToColumnName.get("broker")));
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
