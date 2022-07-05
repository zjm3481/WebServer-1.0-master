package com.study.webserver.core.exception;

import com.study.webserver.core.enumeration.HTTPStatus;
import com.study.webserver.core.exception.base.ServletException;

/**
 * 请求解析异常
 */
public class RequestParseException extends ServletException {
    private static final HTTPStatus status = HTTPStatus.BAD_REQUEST;
    public RequestParseException() {
        super(status);
    }
}
