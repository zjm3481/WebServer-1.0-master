package com.study.webserver.core.servlet.base;

import com.study.webserver.core.enumeration.RequestMethod;
import com.study.webserver.core.exception.base.ServletException;
import com.study.webserver.core.request.Request;
import com.study.webserver.core.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * servlet
 */
@Slf4j
public abstract class HTTPServlet {
    public void service(Request request, Response response) throws ServletException, IOException {
        if (request.getMethod() == RequestMethod.GET) {
            doGet(request, response);
        } else if (request.getMethod() == RequestMethod.POST) {
            doPost(request, response);
        } else if (request.getMethod() == RequestMethod.PUT) {
            doPut(request, response);
        } else if (request.getMethod() == RequestMethod.DELETE) {
            doDelete(request, response);
        }
    }

    public void doGet(Request request, Response response) throws ServletException, IOException {
    }

    public void doPost(Request request, Response response) throws ServletException, IOException {
    }

    public void doPut(Request request, Response response) throws ServletException, IOException {
    }

    public void doDelete(Request request, Response response) throws ServletException, IOException {
    }


}
