package com.inov8.integration.middleware.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JSONUtil {

    private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    public static String getJSON(Object obj){

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error(" Object to json JsonProcessingException ",e);
        }
        return null;
    }

    public static Object jsonToObject(String json, Class object) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, object);
        } catch (Exception e) {
            logger.error("Exception Occured during Json to object Parsing ",e);
            return null;
        }
    }
}
