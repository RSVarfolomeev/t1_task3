package t1.school.springstarter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "http-logger")
public class HttpLoggerProperties {

    /**
     * Включение/выключение стартера логгирования http запросов.
     * Допустимые значения: true - включено, false - выключено.
     */
    private Boolean enabled = false;

    /**
     * Уровень логирования для входящих http запросов.
     * Допустимые значения: trace, debug, info, warn, error.
     */
    private String preHandleLogLevel = "info";

    /**
     * Уровень логирования для исходящих http запросов.
     * Допустимые значения: trace, debug, info, warn, error.
     */
    private String postHandleLogLevel = "info";

    /**
     * Уровень логирования для http запросов, при обработке которых возникло исключение.
     * Допустимые значения: trace, debug, info, warn, error.
     */
    private String afterCompletionLogLevel = "error";

    /**
     * Включение/выключение заголовков запросов в логе.
     * Допустимые значения: true - включено, false - выключено.
     */
    private Boolean headersEnabled = false;
}
