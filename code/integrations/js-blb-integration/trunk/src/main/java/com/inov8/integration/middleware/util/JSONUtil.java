package com.inov8.integration.middleware.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtil {

    private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    public static String getJSON(Object obj) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error(" Object to json JsonProcessingException ", e);
        }
        return null;
    }

    public static Object jsonToObject(String json, Class object) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(json, object);
        } catch (Exception e) {
            logger.error("Exception Occured during Json to object Parsing ", e);
            return null;
        }
    }

    public static void main(String[] args) {


    }

}
