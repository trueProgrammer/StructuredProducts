package com.structuredproducts.sevices;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Charsets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ServiceUtils {

    public static Map<String, Object> getObjectMapping(String request) throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        return mapper.readValue(
                new ByteArrayInputStream(request.getBytes(Charsets.UTF_8)), Map.class);
    }

    public static <T> List<T> getObjects(Class<T> clazz, String json) throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        return mapper.readValue(new ByteArrayInputStream(json.getBytes(Charsets.UTF_8)),
                TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
    }

    public static boolean compareDatesWithoutTimes(Date date1, Date date2) {
        return date1.getDay() == date2.getDay() && date1.getMonth() == date2.getMonth() && date1.getYear() == date2.getYear();
    }

}
