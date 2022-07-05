package com.study.webserver.core;

import com.study.webserver.core.servlet.context.ServletContext;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class WebApplication {
    private static ServletContext servletContext = new ServletContext();
    
    public static ServletContext getServletContext() {
        return servletContext;
    }
}
