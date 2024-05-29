package t1.school.springstarter.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import t1.school.springstarter.config.HttpLoggerProperties;

import java.util.Collection;
import java.util.Collections;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {

    private final HttpLoggerProperties httpLoggerProperties;

    public LoggerInterceptor(HttpLoggerProperties httpLoggerProperties) {
        this.httpLoggerProperties = httpLoggerProperties;
    }

    /**
     * Выполняется перед обработкой запроса.
     **/
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String headers = "";
        if (httpLoggerProperties.getHeadersEnabled()) {
            Collection<String> headerNames = Collections.list(request.getHeaderNames());

            StringBuilder headersSb = new StringBuilder();
            headerNames.forEach(header -> headersSb.append(header)
                            .append(": ")
                            .append(request.getHeader(header))
                            .append(System.lineSeparator()));
            headers = headersSb.toString();
        }

        String message = "Данные запроса:"
                + System.lineSeparator() + request.getMethod()
                + System.lineSeparator() + request.getRequestURI()
                + System.lineSeparator() + headers;

        sendLog(httpLoggerProperties.getPreHandleLogLevel(), message);
        return true;
    }

    /**
     * Выполняется после обработки запроса.
     **/
    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
        String headers = "";
        if (httpLoggerProperties.getHeadersEnabled()) {
            Collection<String> headerNames = response.getHeaderNames();

            StringBuilder headersSb = new StringBuilder();
            headerNames.forEach(header -> headersSb.append(header)
                    .append(": ")
                    .append(response.getHeader(header))
                    .append(System.lineSeparator()));
            headers = headersSb.toString();
        }

        String message = "Данные ответа:"
                + System.lineSeparator() + response.getStatus()
                + System.lineSeparator() + response.getContentType()
                + System.lineSeparator() + headers;

        sendLog(httpLoggerProperties.getPostHandleLogLevel(), message);
    }

    /**
     * Метод для получения данных запроса и ответа при возникновении исключения.
     **/
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        if (ex != null) {
            String message = "При запросе произошла ошибка:" + System.lineSeparator() + ex;

            sendLog(httpLoggerProperties.getAfterCompletionLogLevel(), message);
        }
    }

    private void sendLog(String level, String message) {
        if (level.equals("trace")) {
            log.trace(message);
        } else if (level.equals("debug")) {
            log.debug(message);
        } else if (level.equals("warn")) {
            log.warn(message);
        } else if (level.equals("error")) {
            log.error(message);
        } else {
            log.info(message);
        }
    }
}