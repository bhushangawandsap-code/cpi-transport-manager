package com.bhushan.cpitransport.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtil() {
        // Utility class
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static String prettyPrint(String json) {
        try {
            return OBJECT_MAPPER
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(
                            OBJECT_MAPPER.readTree(json));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}