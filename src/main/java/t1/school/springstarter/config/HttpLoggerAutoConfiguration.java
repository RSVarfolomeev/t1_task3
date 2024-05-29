package t1.school.springstarter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import t1.school.springstarter.service.LoggerInterceptor;

import java.util.List;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(HttpLoggerProperties.class)
@ConditionalOnProperty(prefix = "http-logger", value = "enabled", havingValue = "true")
public class HttpLoggerAutoConfiguration {

    private final HttpLoggerProperties httpLoggerProperties;

    public HttpLoggerAutoConfiguration(HttpLoggerProperties httpLoggerProperties) {
        this.httpLoggerProperties = httpLoggerProperties;
    }

    @Bean
    public WebMvcConfigurer mvcConfig(List<HandlerInterceptor> loggingInterceptors) {
        log.info("Добавление WebMvcConfigurer");
        return new WebMvcConfig(loggingInterceptors);
    }

    @Bean
    public LoggerInterceptor loggerInterceptor() {
        log.info("Добавление LoggerInterceptor");
        return new LoggerInterceptor(httpLoggerProperties);
    }
}
