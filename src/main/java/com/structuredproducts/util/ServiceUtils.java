package com.structuredproducts.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class ServiceUtils {

    public static Map<String, Object> getObjectMapping(String request) throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        return mapper.readValue(
                new ByteArrayInputStream(request.getBytes(Charsets.UTF_8)), Map.class);
    }

}
