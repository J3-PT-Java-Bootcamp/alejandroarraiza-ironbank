package com.ironhack.ironbankapi;

import com.ironhack.ironbankapi.log.interceptor.RequestLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IronbankApiConfig implements WebMvcConfigurer {

    final
    RequestLoggingInterceptor requestLoggingInterceptor;

    public IronbankApiConfig(RequestLoggingInterceptor requestLoggingInterceptor) {
        this.requestLoggingInterceptor = requestLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor);
    }
}
