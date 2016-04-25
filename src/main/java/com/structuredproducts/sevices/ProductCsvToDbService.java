package com.structuredproducts.sevices;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.structuredproducts.controllers.data.ProductBean;
import com.structuredproducts.controllers.data.Tuple;
import com.structuredproducts.persistence.entities.instrument.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProductCsvToDbService {

    private final static Logger logger = LoggerFactory.getLogger(ProductCsvToDbService.class);

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

    @Autowired
    DBService dbService;

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

    public void convertToDb(InputStreamReader reader, String broker) {
        try {
            List<ProductBean> productList = readCsv(reader);
            List<Product> convertedProducts = convertToProdutsList(productList);
            if(!StringUtils.isEmpty(broker)) {
                convertedProducts.parallelStream().forEach(
                        product -> product.setBroker(product.getBroker() == null ? new Broker(broker) : product.getBroker())
                );
            }
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
            product.setUnderlayingList(productBean.getUnderlying());
            product.setMinInvest(productBean.getMinInvestment());
            product.setMaxInvest(productBean.getMaxInvestment());
            product.setBroker(new Broker(productBean.getBroker()));
            product.setReturnValue(productBean.getProfit());
            product.setStrategy(new Strategy(productBean.getStrategy()));
            product.setLegalType(new LegalType(productBean.getLegalType()));
            product.setPayoff(new PayOff(productBean.getPayoff()));
            product.setRisks(new Risks(productBean.getRisk()));
            product.setMinTerm(productBean.getMinTerm());
            product.setMaxTerm(productBean.getMaxTerm());
            product.setProductType(new ProductType(productBean.getProductType()));
            product.setPaymentPeriodicity(new PaymentPeriodicity(productBean.getPeriodicity()));
            product.setDescription(productBean.getDescription());
            return product;
        }).collect(Collectors.toList());
    }

    public String convertToCsv() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CsvMapWriter writer = new CsvMapWriter(stringWriter, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
        writer.writeHeader(header);
        return stringWriter.toString();
    }

    private static final Pattern FROM_TO_TERM_PATTERN_MONTH = Pattern.compile("от (\\d+) до (\\d+)\\s?мес.*");
    private static final Pattern FROM_DASH_TO_TERM_PATTERN_MONTH = Pattern.compile("(\\d+)\\s?-\\s?(\\d+)\\s?мес.*");
    private static final Pattern FROM_TO_TERM_PATTERN_YEAR = Pattern.compile("от ([\\d\\.\\,]+)\\s?до\\s?([\\d\\.\\,]+)\\s?лет.*");
    private static final Pattern FROM_DASH_TO_TERM_PATTERN_YEAR = Pattern.compile("([\\d\\.\\,]+)\\s?-\\s?([\\d\\.\\,]+)\\s?лет.*");
    private static final Pattern YEAR_PATTERN = Pattern.compile("([\\d\\.\\,]+)\\s?год.*");
    private static final Pattern FROM_YEAR_PATTERN = Pattern.compile("от\\s?([\\d\\.\\,]+)\\s?год.*");
    private static final Pattern FROM_MONTH_PATTERN = Pattern.compile("от\\s?(\\d+)\\s?мес.*");
    private static final Pattern MONTH_PATTERN = Pattern.compile("(\\d+)\\s?мес.*");
    private static final Pattern UNDERLAYING_PATTERN = Pattern.compile("(.*)\\((.*)\\)");
    private static final Pattern FROM_TO_INVEST_PATTERN = Pattern.compile("от (\\d+) до (\\d+) тыс.*");
    private static final Pattern TO_INVEST_PATTERN = Pattern.compile("до (\\d+) тыс.*");
    private static final Pattern INVEST_PATTERN = Pattern.compile(".*([\\d]+).*");

    public List<ProductBean> readCsv(InputStreamReader reader) throws IOException {
        List<ProductBean> result = new ArrayList<>();

        try(ICsvMapReader mapReader = new CsvMapReader(reader, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)) {
            String[] headers = mapReader.getHeader(true);
            Map<String, String> line;
            while( (line = mapReader.read(headers)) != null) {
                List<Tuple> entryList = Lists.newArrayList();

                for(Map.Entry<String, String> entry : line.entrySet()) {
                    if(entry.getKey() == null || entry.getValue() == null) {
                        continue;
                    } else {
                        entryList.add(new Tuple(entry.getKey().toLowerCase().trim(), entry.getValue().trim()));
                    }
                }
                final ProductBean bean = new ProductBean();
                entryList.stream().forEach(
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
                                    Consumer<Matcher> monthMatcher = v -> {
                                        bean.setMinTerm(Integer.parseInt(v.group(1)));
                                        bean.setMaxTerm(Integer.parseInt(v.group(2)));
                                    };
                                    Consumer<Matcher> yearMatcher = v -> {
                                        bean.setMinTerm((int) (Float.parseFloat(v.group(1).replace(",", ".")) * 12));
                                        bean.setMaxTerm((int) (Float.parseFloat(v.group(2).replace(",", ".")) * 12));
                                    };
                                    Matcher fromToMatcherMonth = FROM_TO_TERM_PATTERN_MONTH.matcher(tuple.getValue().toLowerCase());
                                    if (fromToMatcherMonth.matches()) {
                                        monthMatcher.accept(fromToMatcherMonth);
                                        break;
                                    }
                                    Matcher fromDashToMatcherMonth = FROM_DASH_TO_TERM_PATTERN_MONTH.matcher(tuple.getValue().toLowerCase());
                                    if (fromDashToMatcherMonth.matches()) {
                                        monthMatcher.accept(fromDashToMatcherMonth);
                                        break;
                                    }
                                    Matcher fromDashToMatcherYear = FROM_DASH_TO_TERM_PATTERN_YEAR.matcher(tuple.getValue().toLowerCase());
                                    if (fromDashToMatcherYear.matches()) {
                                        yearMatcher.accept(fromDashToMatcherYear);
                                        break;
                                    }
                                    Matcher fromToMatcherYear = FROM_TO_TERM_PATTERN_YEAR.matcher(tuple.getValue().toLowerCase());
                                    if (fromToMatcherYear.matches()) {
                                        yearMatcher.accept(fromToMatcherYear);
                                        break;
                                    }
                                    Matcher fromYearPatter = FROM_YEAR_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if (fromYearPatter.matches()) {
                                        bean.setMinTerm((int) (Double.parseDouble(fromYearPatter.group(1).replace(",", ".")) * 12));
                                        break;
                                    }
                                    Matcher yearPatter = YEAR_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if (yearPatter.matches()) {
                                        bean.setMaxTerm((int) (Double.parseDouble(yearPatter.group(1).replace(",", ".")) * 12));
                                        break;
                                    }
                                    Matcher fromMonthPatter = FROM_MONTH_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if (fromMonthPatter.matches()) {
                                        bean.setMaxTerm(Integer.parseInt(fromMonthPatter.group(1)));
                                        break;
                                    }
                                    Matcher monthPatter = MONTH_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if (monthPatter.matches()) {
                                        bean.setMaxTerm(Integer.parseInt(monthPatter.group(1)));
                                        break;
                                    }
                                    throw new RuntimeException("Unknown term:" + tuple.getValue());
                                case "базовый актив":
                                case "базовые актив":
                                case "underlayings":
                                case "underlying-1":
                                case "underlying":
                                    Function<String, Iterable<String>> splitFunction = v -> Splitter.on(",").split(v);
                                    List<Underlaying> underObjList = Lists.newArrayList();
                                    Iterable<String> underlayings;
                                    Matcher underMatcher = UNDERLAYING_PATTERN.matcher(tuple.getValue());
                                    if (underMatcher.matches()) {
                                        String type = underMatcher.group(1);
                                        underlayings = splitFunction.apply(underMatcher.group(2));
                                        underlayings.forEach(
                                                str -> {
                                                    Underlaying under = new Underlaying(str.trim());
                                                    under.setType(new UnderlayingType(type));
                                                    underObjList.add(under);
                                                }
                                        );
                                    } else {
                                        underlayings = splitFunction.apply(tuple.getValue());
                                        underlayings.forEach(
                                                str -> underObjList.add(new Underlaying(str.trim()))
                                        );
                                    }
                                    bean.setUnderlying(underObjList);
                                    break;
                                case "доходность":
                                case "потенциальная доходность (% годовых)":
                                case "return":
                                    bean.setProfit(Float.parseFloat(tuple.getValue().replace("%", "").replace(",", ".")));
                                    break;
                                case "стратегия":
                                case "strategy":
                                    bean.setStrategy(tuple.getValue());
                                    break;
                                case "валюта":
                                case "currency":
                                    String currency = currencyMap.get(tuple.getValue().toLowerCase());
                                    if (currency == null) {
                                        logger.error("Unknown currency:" + tuple.getValue());
                                        throw new RuntimeException("Unknown currency:" + tuple.getValue());
                                    }
                                    bean.setCurrency(currency);
                                    break;
                                case "минимальная сумма инвестиций":
                                case "сумма инвестиций":
                                case "min investment":
                                case "mininvestment":
                                case "investment":
                                    Matcher fromToInvestMatcher = FROM_TO_INVEST_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if (fromToInvestMatcher.matches()) {
                                        bean.setMinInvestment(Integer.parseInt(fromToInvestMatcher.group(1)) * 1000);
                                        bean.setMaxInvestment(Integer.parseInt(fromToInvestMatcher.group(2)) * 1000);
                                        break;
                                    }
                                    Matcher toInvestMatcher = TO_INVEST_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if (toInvestMatcher.matches()) {
                                        bean.setMaxInvestment(Integer.parseInt(toInvestMatcher.group(1)) * 1000);
                                        break;
                                    }

                                    Matcher investMatcher = INVEST_PATTERN.matcher(tuple.getValue().toLowerCase());
                                    if (investMatcher.matches()) {
                                        bean.setMaxInvestment(Integer.parseInt(investMatcher.group(1).replaceAll("\\s","")));
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
                                case "broker":
                                case "брокер":
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
                                    logger.error("Unknown column:" + tuple.getName());
                                    //throw new RuntimeException("Unknown column:" + tuple.getName());
                            }
                        }
                );
                if (bean.getBroker() != null) {
                    result.add(bean);
                }
            }
        }
        return result;
    }
}
