package t1.school.springstarter.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class WebMvcConfig implements WebMvcConfigurer {
    private final List<HandlerInterceptor> loggingInterceptors;

    public WebMvcConfig(List<HandlerInterceptor> loggingInterceptors) {
        this.loggingInterceptors = loggingInterceptors;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        loggingInterceptors.forEach(registry::addInterceptor);
    }
}
