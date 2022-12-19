package com.inov8.integration.middleware.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inov8.integration.middleware.util.ConfigurationUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestConfiguration {

    private static String READ_TIMEOUT = ConfigurationUtil.getValue("connection.read-timeout", "60");

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        requestFactory.setHttpClient(httpClient);

        int seconds = Integer.parseInt(READ_TIMEOUT);
        requestFactory.setReadTimeout(seconds * 1000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);


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
