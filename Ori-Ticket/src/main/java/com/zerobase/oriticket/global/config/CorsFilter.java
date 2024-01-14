package com.zerobase.oriticket.global.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        List<String> allowUrl = List.of(
                "https://jxy.me",               // chat debug site url
                "http://localhost:8080",
                "http://127.0.0.1:8080",
                "https://ori-ticket.vercel.app"
        );
        String origin = request.getHeader("origin");
        log.info("origin url : "+origin);

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Authorization");

        if(origin != null && allowUrl.contains(origin)){
            log.info("allow access : " + origin);
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        chain.doFilter(req, res);

    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}
