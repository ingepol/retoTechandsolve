package com.mudanza.server.main;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
* Filtro para cofigurar un CORS simple para la aplicación.
* 
* 
* @author Paul Andrés Arenas Cardona
* @version 1.0 
* 
* Fecha de creación 2018-05-07
* 
*/
@Component
public class SimpleCORSFilter implements Filter {

	private static final Logger logger = LogManager.getLogger(SimpleCORSFilter.class);
	
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Content-Range, Accept");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        chain.doFilter(req, res); 
    }

    public void init(FilterConfig filterConfig) {
    	logger.debug("Init Simple CORS Filter");
    }

    public void destroy() {
    	logger.debug("Destroy Simple CORS Filter");
    }
}