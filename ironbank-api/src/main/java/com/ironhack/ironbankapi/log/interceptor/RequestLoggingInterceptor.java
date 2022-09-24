package com.ironhack.ironbankapi.log.interceptor;

import com.ironhack.ironbankapi.log.model.LogItem;
import com.ironhack.ironbankapi.log.repository.LogItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Autowired
    private LogItemRepository logItemRepository;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        var logItem = LogItem
                .builder()
                .timestamp(Instant.now())
                .method(request.getMethod())
                .requestPath(request.getRequestURL().toString())
                .responseStatus(String.valueOf(response.getStatus()))
                .build();
        logItemRepository.save(logItem);
        System.out.println(logItem);
    }
}
