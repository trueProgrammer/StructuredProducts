package com.structuredproducts.sevices;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.structuredproducts.controllers.data.ProductBean;
import com.structuredproducts.controllers.data.Tuple;
import com.structuredproducts.persistence.entities.instrument.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.Optional;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProductCsvToDbService {

    private final static Logger logger = LoggerFactory.getLogger(ProductCsvToDbService.class);

    /*public final static CellProcessor[] PRODUCTS_PROCESSORS = new CellProcessor[]{
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
    };*/

    private static final Map<String, String> currencyMap =
            ImmutableMap.<String, String>builder()
            .put("рубли", "RUR")
            .put("рубль", "RUR")
            .put("ruble", "RUR")
            .put("rur", "RUR")
            .put("российские рубли", "RUR")
            .put("доллары сша", "USD")
            .put("доллары", "USD")
            .put("usd", "USD")
            .put("евро", "EUR")
            .put("eur", "EUR")
            .build();

    public final static CellProcessor[] PRODUCTS_PROCESSORS = new CellProcessor[]{
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
    };

    @Autowired
    DBService dbService;

    private Map<String, String> beanPropertiesToColumnName = new HashMap<>();
    {
        beanPropertiesToColumnName.put("name", "Название");//��������
        beanPropertiesToColumnName.put("productType", "Тип продукта");
        beanPropertiesToColumnName.put("minTerm","Минимальный срок");
        beanPropertiesToColumnName.put("maxTerm", "Максимальный срок");
        beanPropertiesToColumnName.put("underlying", "Базовый актив");
        beanPropertiesToColumnName.put("minInvestment","Минимальная сумма");
        beanPropertiesToColumnName.put("maxInvestment","Максимальная сумма");
        beanPropertiesToColumnName.put("broker","Провайдер");
        beanPropertiesToColumnName.put("return","Доходность");
        beanPropertiesToColumnName.put("strategy", "Стратегия");
        beanPropertiesToColumnName.put("legalType", "Юридическая форма");
        beanPropertiesToColumnName.put("payoff","Размер выплаты");
        beanPropertiesToColumnName.put("risks", "Риски");
        beanPropertiesToColumnName.put("currency", "Валюта");
        beanPropertiesToColumnName.put("periodicity", "Периодичность выплат");
    }

    private String header[] = new String[]{
            "Название",
            "Тип продукта",
            "Минимальный срок",
            "Максимальный срок",
            "Базовый актив",
            "Минимальная сумма",
            "Максимальная сумма",
            "Провайдер",
            "Доходность",
            "Стратегия",
            "Юридическая форма",
            "Размер выплаты",
            "Риски",
            "Валюта",
            "Периодичность выплат"
    };

    public void convertToDb(InputStreamReader reader) {
        try {
            List<ProductBean> productList = readCsv(reader);
            List<Product> convertedProducts = convertToProdutsList(productList);
            dbService.saveProducts(convertedProducts);
        } catch (IOException e) {
            logger.error("Error while import csv.", e);
            throw new RuntimeException(e);
        }
    }

    public List<Product> convertToProdutsList(List<ProductBean> productList) throws IOException {
        return productList.stream().<Product>map(productBean -> {
            Product product = new Product();
            product.setName(productBean.getName());
            product.setCurrency(new Currency(productBean.getCurrency()));
            //product.setUnderlaying(new Underlaying(productBean.getUnderlying()));
            product.setInvestment(new Investment(productBean.getMinInvestment(), productBean.getMaxInvestment()));
            product.setBroker(new Broker(productBean.getBroker()));
            //product.setReturnValue(new Return(productBean.getProfit()));
            product.setStrategy(new Strategy(productBean.getStrategy()));
            product.setLegalType(new LegalType(productBean.getLegalType()));
            product.setPayoff(new PayOff(productBean.getPayoff()));
            product.setRisks(new Risks(productBean.getRisk()));
            product.setTerm(new Term(productBean.getMinTerm(), productBean.getMaxTerm()));
            product.setProductType(new ProductType(productBean.getProductType()));
            product.setPaymentPeriodicity(new PaymentPeriodicity(productBean.getPeriodicity()));
            return product;
        }).collect(Collectors.toList());
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

    private static final Pattern FROM_TO_TERM_PATTERN = Pattern.compile("от (\\d+) до (\\d+) мес.*");
    private static final Pattern YEAR_PATTERN = Pattern.compile("([\\d\\.\\,]+)\\sгод.*");
    private static final Pattern UNDERLAYING_PATTERN = Pattern.compile("(\\w+)\\s*\\(([\\w\\S\\s])\\)+");
    private static final Pattern FROM_TO_INVEST_PATTERN = Pattern.compile("от (\\d+) до (\\d+) тыс.*");
    private static final Pattern TO_INVEST_PATTERN = Pattern.compile("до (\\d+) тыс.*");

    List<ProductBean> readCsv(InputStreamReader reader) throws IOException {
        List<ProductBean> result = new ArrayList<>();

        try(ICsvMapReader mapReader = new CsvMapReader(reader, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)) {
            String[] headers = mapReader.getHeader(true);
            Map<String, String> line;
            while( (line = mapReader.read(headers)) != null) {
                List<Tuple> entryList = Lists.newArrayList();

                line.entrySet().parallelStream().forEach(
                        entry -> entryList.add(new Tuple(entry.getKey().toLowerCase().trim(), entry.getValue().trim()))
                );
                final ProductBean bean = new ProductBean();
                entryList.parallelStream().forEach(
                        tuple -> {

                            switch (tuple.getName()) {
                                case "название":
                                case "наименование":
                                case "name":
                                    bean.setName(tuple.getValue());
                                    break;
                                case "тип продукта":
                                case "тип структурного продукта":
                                case "type":
                                    bean.setProductType(tuple.getValue());
                                    break;
                                case "описание":
                                case "описание продукта":
                                case "description":
                                    bean.setDescription(tuple.getValue());
                                    break;
                                case "срок":
                                case "term":
                                    Matcher fromToMatcher = FROM_TO_TERM_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if(fromToMatcher.matches()) {
                                        bean.setMinTerm(Integer.parseInt(fromToMatcher.group(1)));
                                        bean.setMaxTerm(Integer.parseInt(fromToMatcher.group(2)));
                                        break;
                                    }
                                    Matcher yearPatter = YEAR_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if(yearPatter.matches()) {
                                        bean.setMaxTerm((int) (Double.parseDouble(yearPatter.group(1).replace(",",".")) * 12));
                                        break;
                                    }
                                    throw new RuntimeException("Unknown term:" + tuple.getValue());
                                case "базовый актив":
                                case "Underlying-1":
                                case "Underlying":
                                    Matcher underlayingPattern = UNDERLAYING_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if(underlayingPattern.matches()) {
                                        bean.setUnderlying2(underlayingPattern.group(1));
                                        Iterable<String> underlayings = Splitter.on(",").split(underlayingPattern.group(2));
                                        bean.setUnderlying(Lists.newArrayList(underlayings));
                                    } else {
                                        Iterable<String> underlayings = Splitter.on(",").split(tuple.getValue());
                                        bean.setUnderlying(Lists.newArrayList(underlayings));
                                    }
                                    break;
                                case "доходность":
                                case "return":
                                    bean.setReturn(Double.parseDouble(tuple.getValue().replace("%", "").replace(",", ".")));
                                    break;
                                case "стратегия":
                                case "strategy":
                                    bean.setStrategy(tuple.getValue());
                                    break;
                                case "валюта":
                                case "currency":
                                    String currency = currencyMap.get(tuple.getValue().toLowerCase());
                                    if(currency == null) {
                                        logger.error("Unknown currency:" + tuple.getValue());
                                        throw new RuntimeException("Unknown currency:" + tuple.getValue());
                                    }
                                    bean.setCurrency(currency);
                                    break;
                                case "минимальная сумма инвестиций":
                                case "сумма инвестиций":
                                case "min investment":
                                case "investment":
                                    Matcher fromToInvestMatcher = FROM_TO_INVEST_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if(fromToInvestMatcher.matches()) {
                                        bean.setMinInvestment(Integer.parseInt(fromToInvestMatcher.group(1)) * 1000);
                                        bean.setMaxInvestment(Integer.parseInt(fromToInvestMatcher.group(2))*1000);
                                        break;
                                    }
                                    Matcher toInvestMatcher = TO_INVEST_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if(toInvestMatcher.matches()) {
                                        bean.setMaxInvestment(Integer.parseInt(toInvestMatcher.group(1))*1000);
                                        break;
                                    }
                                    throw new RuntimeException("Unknown invest:" + tuple.getValue());
                                case "минимальный срок":
                                    bean.setMinTerm(Integer.parseInt(tuple.getValue()));
                                    break;
                                case "максимальный срок":
                                    bean.setMaxTerm(Integer.parseInt(tuple.getValue()));
                                    break;
                                case "минимальная сумма":
                                    bean.setMinInvestment(Integer.parseInt(tuple.getValue()));
                                    break;
                                case "максимальная сумма":
                                    bean.setMaxInvestment(Integer.parseInt(tuple.getValue()));
                                    break;
                                case "провайдер":
                                    bean.setBroker(tuple.getValue());
                                    break;
                                case "юридическая форма":
                                    bean.setLegalType(tuple.getValue());
                                    break;
                                case "размер выплаты":
                                    bean.setPayoff(tuple.getValue());
                                    break;
                                case "риски":
                                    bean.setRisk(tuple.getValue());
                                    break;
                                case "периодичность выплат":
                                    bean.setPeriodicity(tuple.getValue());
                                    break;
                                default:
                                    throw new RuntimeException("Unknown column:" + tuple.getName());
                            }
                        }
                );
                result.add(bean);
            }
        }
        return result;
    }
}
