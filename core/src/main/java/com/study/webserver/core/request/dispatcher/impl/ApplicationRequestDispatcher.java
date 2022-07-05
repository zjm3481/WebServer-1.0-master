package com.study.webserver.core.request.dispatcher.impl;

import com.study.webserver.core.constant.CharsetProperties;
import com.study.webserver.core.enumeration.HTTPStatus;
import com.study.webserver.core.exception.ResourceNotFoundException;
import com.study.webserver.core.exception.base.ServletException;
import com.study.webserver.core.request.Request;
import com.study.webserver.core.request.dispatcher.RequestDispatcher;
import com.study.webserver.core.resource.ResourceHandler;
import com.study.webserver.core.response.Response;
import com.study.webserver.core.util.IOUtil;
import com.study.webserver.core.util.MimeTypeUtil;
import com.study.webserver.core.template.TemplateResolver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by SinjinSong on 2017/7/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ApplicationRequestDispatcher implements RequestDispatcher {
    private String url;
    
    @Override
    public void forward(Request request, Response response) throws ServletException, IOException {
        if (ResourceHandler.class.getResource(url) == null) {
            throw new ResourceNotFoundException();
        }
        log.info("forward至 {} 页面",url);
        String body = TemplateResolver.resolve(new String(IOUtil.getBytesFromFile(url), CharsetProperties.UTF_8_CHARSET),request);
        response.header(HTTPStatus.OK, MimeTypeUtil.getTypes(url)).body(body.getBytes(CharsetProperties.UTF_8_CHARSET));
    }
}
