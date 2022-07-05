package com.study.webserver.core;

import com.study.webserver.core.servlet.context.ServletContext;

/**
 *
 */
public class WebApplication {
    private static ServletContext servletContext = new ServletContext();
    
    public static ServletContext getServletContext() {
        return servletContext;
    }
}
