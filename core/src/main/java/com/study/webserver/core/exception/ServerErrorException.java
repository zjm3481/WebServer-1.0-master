package com.study.webserver.core.exception;

import com.study.webserver.core.enumeration.HTTPStatus;
import com.study.webserver.core.exception.base.ServletException;

/**
 * 服务器错误
 */
public class ServerErrorException extends ServletException {
    private static final HTTPStatus status = HTTPStatus.INTERNAL_SERVER_ERROR;
    public ServerErrorException() {
        super(status);
    }
}
