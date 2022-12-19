package com.inov8.integration.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

public class PropertyReader extends PropertyPlaceholderConfigurer {
    private static Map propertiesMap;

    public static String getProperty(String name) {
        Object parameter = propertiesMap.get(name);
        return (parameter == null) ? null : parameter.toString();
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory,
                                     Properties props) throws BeansException {
        super.processProperties(beanFactory, props);

        propertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            propertiesMap.put(keyStr, parseStringValue(props.getProperty(keyStr),
                    props, new HashSet()));
        }
    }
}
