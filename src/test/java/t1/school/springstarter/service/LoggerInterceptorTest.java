package t1.school.springstarter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import t1.school.springstarter.config.HttpLoggerProperties;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = LoggerInterceptor.class)
class LoggerInterceptorTest {

    @MockBean
    private HttpLoggerProperties httpLoggerProperties;

    @Autowired
    private LoggerInterceptor loggerInterceptor;

    @Test
    void preHandle() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.GET.name());
        request.setRequestURI("/test");
        request.addHeader("Name", "Value");

        Mockito.when(httpLoggerProperties.getHeadersEnabled()).thenReturn(true);
        Mockito.when(httpLoggerProperties.getPreHandleLogLevel()).thenReturn("info");
        boolean result = loggerInterceptor.preHandle(request, response, mock(Object.class));

        //Проверка, что возвращается true из метода
        Assertions.assertTrue(result);

        //Проверка, что вызывается метод getHeadersEnabled 1 раз
        verify(httpLoggerProperties, times(1)).getHeadersEnabled();

        //Проверка содержимого запроса
        Assertions.assertEquals("Value", request.getHeader("Name"));
        Assertions.assertEquals("/test", request.getRequestURI());
    }

    @Test
    void postHandle() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setContentType("application/json");
        response.setStatus(200);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.GET.name());
        request.setRequestURI("/test");
        request.addHeader("Name", "Value");

        Mockito.when(httpLoggerProperties.getHeadersEnabled()).thenReturn(true);
        Mockito.when(httpLoggerProperties.getPostHandleLogLevel()).thenReturn("info");
        loggerInterceptor.postHandle(request, response, mock(Object.class), mock(ModelAndView.class));

        //Проверка, что вызывается метод getHeadersEnabled 1 раз
        verify(httpLoggerProperties, times(1)).getHeadersEnabled();

        //Проверка содержимого запроса
        Assertions.assertEquals("Value", request.getHeader("Name"));
        Assertions.assertEquals("/test", request.getRequestURI());

        //Проверка содержимого ответа
        Assertions.assertEquals("application/json", response.getContentType());
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void afterCompletion() {
        // Перенаправляем System.out в ByteArrayOutputStream для перехвата вывода в лог
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(500);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.GET.name());
        request.setRequestURI("/test");

        RuntimeException ex = new RuntimeException("Тестовое исключение");

        Mockito.when(httpLoggerProperties.getAfterCompletionLogLevel()).thenReturn("error");

        loggerInterceptor.afterCompletion(request, response, mock(Object.class), ex);

        //Проверка содержимого исключения
        Assertions.assertEquals("Тестовое исключение", ex.getMessage());

        // Восстанавливаем System.out
        System.setOut(System.out);
        // Проверяем, что строка "Тестовое исключение" из выброшенного исключения была выведена
        Assertions.assertTrue(outputStream.toString().contains("Тестовое исключение"));
    }
}