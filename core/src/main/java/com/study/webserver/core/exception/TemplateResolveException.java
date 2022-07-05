package com.study.webserver.core.exception;

import com.study.webserver.core.enumeration.HTTPStatus;
import com.study.webserver.core.exception.base.ServletException;

/**
 * 模板异常
 */
public class TemplateResolveException extends ServletException {
    private static final HTTPStatus status = HTTPStatus.INTERNAL_SERVER_ERROR;
    public TemplateResolveException() {
        super(status);
    }
}
   
