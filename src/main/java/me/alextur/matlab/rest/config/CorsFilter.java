package me.alextur.matlab.rest.config;

import org.apache.catalina.connector.RequestFacade;
import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Alex Turchynovich
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilter implements Filter {

    private static final Logger logger = Logger.getLogger(CorsFilter.class);

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, X-ACCESS-TOKEN");
        response.setHeader("Access-Control-Max-Age", "3600");

        try {
            chain.doFilter(req, res);
        } catch (IOException | ServletException pE) {
            logger.error("Erorr", pE);
        }
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}

}

