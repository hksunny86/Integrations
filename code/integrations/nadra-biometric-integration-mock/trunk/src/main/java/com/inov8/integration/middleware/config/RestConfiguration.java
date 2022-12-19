package com.inov8.integration.middleware.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
                jsonConverter.setObjectMapper(new ObjectMapper());
                List<MediaType> list = new ArrayList<MediaType>();
                list.add(new MediaType("application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET));
                list.add(new MediaType("text", "javascript", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET));
                list.add(new MediaType("text", "html", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET));
                jsonConverter.setSupportedMediaTypes(list);
            }
        }
        return restTemplate;
    }
}
