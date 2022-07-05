package com.study.webserver.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 构建响应报文的键值对
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {
    private String key;
    private String value;
}
