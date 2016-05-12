package com.structuredproducts.sevices;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface ChartDataService {
    Map<String, String> getChartData(String symbol) throws ExecutionException;
}
