package com.we.broke.payment.service.config;

import com.stripe.net.RequestOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${payment.stripe.api-key}")
    private String apiKey;

    @Bean
    public RequestOptions requestOptions() {
        RequestOptions.RequestOptionsBuilder requestOptionsBuilder =
                new RequestOptions.RequestOptionsBuilder();
        requestOptionsBuilder.setApiKey(apiKey);
        return requestOptionsBuilder.build();
    }
}
