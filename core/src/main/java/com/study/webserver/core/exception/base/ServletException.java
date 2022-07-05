package com.study.webserver.core.exception.base;

import com.study.webserver.core.enumeration.HTTPStatus;
import lombok.Getter;

/**
 * 异常
 */
@Getter
public class ServletException extends Exception {
    private HTTPStatus status;
    public ServletException(HTTPStatus status){
        this.status = status;
    }
}
