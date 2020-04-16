package ru.eltex.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Класс-конфигурация Mvc config.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * Стандартный метод, который позволяет создавать простые автоматизированные контроллеры,
     * предварительно настроенные с кодом состояния и/или видом.
     *
     * @param registry предполагалось создание стандартной формы логина
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/authorization").setViewName("authorization");
    }

}
