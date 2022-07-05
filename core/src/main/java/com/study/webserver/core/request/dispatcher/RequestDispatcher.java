package com.study.webserver.core.request.dispatcher;

import com.study.webserver.core.exception.base.ServletException;
import com.study.webserver.core.request.Request;
import com.study.webserver.core.response.Response;

import java.io.IOException;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public interface RequestDispatcher {
    
    void forward(Request request, Response response)  throws ServletException, IOException;
}
