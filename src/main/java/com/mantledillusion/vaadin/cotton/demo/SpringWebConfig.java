package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.Blueprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
import org.springframework.web.servlet.handler.SimpleServletPostProcessor;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import javax.servlet.*;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SpringWebConfig implements Blueprint {

    @Bean
    public HandlerAdapter handlerAdapter() {
        return new SimpleServletHandlerAdapter();
    }

    @Bean
    public HandlerMapping handlerMapping(@Autowired Servlet demoServlet) {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        Map<String, Object> urls = new HashMap<>();
        urls.put("/*", demoServlet);
        mapping.setUrlMap(urls);
        return mapping;
    }

    @Bean
    public Servlet vaadinServlet() {
        return new DemoServlet(this);
    }

    @Bean
    public SimpleServletPostProcessor simpleServletPostProcessor() {
        return new SimpleServletPostProcessor();
    }
}
