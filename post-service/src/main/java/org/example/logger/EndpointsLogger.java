package org.example.logger;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Component
public class EndpointsLogger implements ApplicationListener<ContextRefreshedEvent> {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public EndpointsLogger(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println(">>> Зарегистрированные эндпоинты:");
        for (Map.Entry<?, ?> entry : requestMappingHandlerMapping.getHandlerMethods().entrySet()) {
            System.out.println(entry.getKey());
        }
    }
}