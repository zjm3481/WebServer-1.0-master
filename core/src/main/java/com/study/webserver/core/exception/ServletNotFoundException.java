package com.study.webserver.core.exception;

import com.study.webserver.core.enumeration.HTTPStatus;
import com.study.webserver.core.exception.base.ServletException;

/**
 * 找不到servlet
 */
public class ServletNotFoundException extends ServletException {
    private static final HTTPStatus status = HTTPStatus.NOT_FOUND;
    public ServletNotFoundException() {
        super(status);
    }
}
