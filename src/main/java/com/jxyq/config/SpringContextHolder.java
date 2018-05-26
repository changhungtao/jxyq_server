package com.jxyq.config;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;
    private @Value("${web.staticPath}") String staticPath;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
        
        if (applicationContext instanceof WebApplicationContext) {
            ServletContext servletContext = ((WebApplicationContext)applicationContext).getServletContext();
            String contextPath = servletContext.getContextPath();
            
            if (StringUtils.isEmpty(staticPath)) {
                staticPath = contextPath;
            }

            servletContext.setAttribute("staticPath", staticPath);
            servletContext.setAttribute("contextPath", contextPath);
        }
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }
}