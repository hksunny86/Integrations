package com.inov8.integration.middleware.util;

import org.apache.commons.configuration.*;
import org.apache.commons.lang.StringUtils;

/**
 * Created by ZeeshanAh1 on 10/26/2015.
 */
public class ConfigurationUtil {

    private static CompositeConfiguration config = new CompositeConfiguration();

    static {
        config.addConfiguration(new SystemConfiguration());
        try {
            config.addConfiguration(new PropertiesConfiguration("application.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key){
        return config.getString(key);
    }

    public static String getValue(String key, String val){

        String resp = config.getString(key);
        return StringUtils.isNotEmpty(resp) ? resp : val;
    }

    public static void main(String[] args) {
        System.out.println(getValue("i8.ip"));
        System.out.println(getValue("i8.port"));
    }

}
