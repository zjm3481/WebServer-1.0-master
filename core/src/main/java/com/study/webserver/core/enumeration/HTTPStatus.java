package com.study.webserver.core.enumeration;

/**
 * Created by SinjinSong on 2017/7/20.
 */
public enum HTTPStatus {
    OK(200),NOT_FOUND(404),INTERNAL_SERVER_ERROR(500),BAD_REQUEST(400),MOVED_TEMPORARILY(302);
    private int code;
    HTTPStatus(int code){
        this.code = code;
    }
    public int getCode(){
        return code;
    }
}
