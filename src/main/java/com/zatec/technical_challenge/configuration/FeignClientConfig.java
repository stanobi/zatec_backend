package com.zatec.technical_challenge.configuration;

import com.zatec.technical_challenge.rest.ChuckFeignClient;
import com.zatec.technical_challenge.rest.SwapiFeignClient;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableFeignClients(clients = {ChuckFeignClient.class, SwapiFeignClient.class})
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            requestTemplate.header(HttpHeaders.CONTENT_TYPE, "application/json");
            requestTemplate.header(HttpHeaders.ACCEPT, "application/json");
            requestTemplate.header(HttpHeaders.USER_AGENT, "Application");
        };
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
