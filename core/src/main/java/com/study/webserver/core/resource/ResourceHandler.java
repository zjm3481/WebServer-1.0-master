package com.study.webserver.core.resource;

import com.study.webserver.core.constant.CharsetProperties;
import com.study.webserver.core.enumeration.HTTPStatus;
import com.study.webserver.core.exception.RequestParseException;
import com.study.webserver.core.exception.ResourceNotFoundException;
import com.study.webserver.core.exception.base.ServletException;
import com.study.webserver.core.exception.handler.ExceptionHandler;
import com.study.webserver.core.request.Request;
import com.study.webserver.core.response.Response;
import com.study.webserver.core.template.TemplateResolver;
import com.study.webserver.core.util.IOUtil;
import com.study.webserver.core.util.MimeTypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class ResourceHandler {
    private ExceptionHandler exceptionHandler;

    public ResourceHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
    
    public void handle(Request request, Response response, Socket client) {
        String url = request.getUrl();
        try {
            if (ResourceHandler.class.getResource(url) == null) {
                log.info("找不到该资源:{}", url);
                throw new ResourceNotFoundException();
            }
            byte[] body = IOUtil.getBytesFromFile(url);
            if (url.endsWith(".html")) {
                body = TemplateResolver
                        .resolve(new String(body, CharsetProperties.UTF_8_CHARSET), request)
                        .getBytes(CharsetProperties.UTF_8_CHARSET);
            }
            response.header(HTTPStatus.OK, MimeTypeUtil.getTypes(url))
                    .body(body)
                    .write();
        } catch (IOException e) {
            exceptionHandler.handle(new RequestParseException(), response, client);
        } catch (ServletException e) {
            exceptionHandler.handle(e, response, client);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}