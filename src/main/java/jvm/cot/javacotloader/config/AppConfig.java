package jvm.cot.javacotloader.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jvm.cot.javacotloader.models.CotEntityMapProvider;
import jvm.cot.javacotloader.models.CotMapper;
import jvm.cot.javacotloader.models.CotResponseMapProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestClient;

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource("classpath:secrets.properties")
public class AppConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public CotResponseMapProvider cotResponseMapProvider() {
        return CotMapper.getResponseMapper();
    }

    @Bean
    public CotEntityMapProvider cotEntityMapProvider() {
        return CotMapper.getEntityMapper();
    }
}
