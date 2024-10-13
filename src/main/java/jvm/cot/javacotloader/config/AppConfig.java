package jvm.cot.javacotloader.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource("classpath:secrets.properties")
public class AppConfig {
}
