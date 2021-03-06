package com.study.webserver.core.request;

import com.study.webserver.core.constant.CharConstant;
import com.study.webserver.core.constant.CharsetProperties;
import com.study.webserver.core.enumeration.RequestMethod;
import com.study.webserver.core.exception.RequestInvalidException;
import com.study.webserver.core.exception.RequestParseException;
import com.study.webserver.core.model.Cookie;
import com.study.webserver.core.model.HTTPSession;
import com.study.webserver.core.request.dispatcher.RequestDispatcher;
import com.study.webserver.core.request.dispatcher.impl.ApplicationRequestDispatcher;
import com.study.webserver.core.servlet.base.RequestHandler;
import com.study.webserver.core.servlet.context.ServletContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>
 * GET /search?hl=zh-CN&source=hp&q=domety&aq=f&oq= HTTP/1.1
 * Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint,
 * application/msword, application/x-silverlight
 * Referer: <a href="http://www.google.cn/">http://www.google.cn/</a>
 * Accept-Language: zh-cn
 * Accept-Encoding: gzip, deflate
 * User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; TheWorld)
 * Host: <a href="http://www.google.cn">www.google.cn</a>
 * Connection: Keep-Alive
 * Cookie: PREF=ID=80a06da87be9ae3c:U=f7167333e2c3b714:NW=1:TM=1261551909:LM=1261551917:S=ybYcq2wpfefs4V9g;
 * NID=31=ojj8d-IygaEtSxLgaJmqSjVhCspkviJrB6omjamNrSm8lZhKy_yMfO2M4QMRKcH1g0iQv9u-2hfBW7bUFwVh7pGaRUb0RnHcJU37y-
 * FxlRugatx63JLv7CWMD6UB_O_r
 * <p>
 * POST /search HTTP/1.1
 * Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint,
 * application/msword, application/x-silverlight
 * Referer: <a href="http://www.google.cn/">http://www.google.cn/</a>
 * Accept-Language: zh-cn
 * Accept-Encoding: gzip,deflate
 * User-Agent: Mozilla/4.0(compatible;MSIE6.0;Windows NT5.1;SV1;.NET CLR2.0.50727;TheWorld)
 * Host: <a href="http://www.google.cn">www.google.cn</a>
 * Connection: Keep-Alive
 * Cookie: PREF=ID=80a06da87be9ae3c:U=f7167333e2c3b714:NW=1:TM=1261551909:LM=1261551917:S=ybYcq2wpfefs4V9g;
 * NID=31=ojj8d-IygaEtSxLgaJmqSjVhCspkviJrB6omjamNrSm8lZhKy_yMfO2M4QMRKcH1g0iQv9u-2hfBW7bUFwVh7pGaRUb0RnHcJU37y-
 * FxlRugatx63JLv7CWMD6UB_O_r
 * <p>
 * hl=zh-CN&source=hp&q=domety
 */

@Getter
@Setter
@Slf4j
public class Request {
    private RequestHandler requestHandler;
    private RequestMethod method;
    private String url;
    private Map<String, List<String>> params;
    private Map<String, List<String>> headers;
    private Map<String, Object> attributes;
    private ServletContext servletContext;
    private Cookie[] cookies;
    private HTTPSession session;

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @param in
     * @throws RequestParseException
     */
    public Request(InputStream in) throws RequestParseException, RequestInvalidException {
        this.attributes = new HashMap<>();
        log.info("????????????Request");
        BufferedInputStream bin = new BufferedInputStream(in);
        byte[] buf = null;
        try {
            buf = new byte[bin.available()];
            int len = bin.read(buf);
            if (len <= 0) {
                throw new RequestInvalidException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] lines = null;
        try {
            //??????????????????????????????URL??????
            lines = URLDecoder.decode(new String(buf, CharsetProperties.UTF_8_CHARSET), CharsetProperties.UTF_8).split(CharConstant.CRLF);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("Request????????????");
        log.info("{}", Arrays.toString(lines));
        try {
            parseHeaders(lines);
            if (headers.containsKey("Content-Length") && !headers.get("Content-Length").get(0).equals("0")) {
                parseBody(lines[lines.length - 1]);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RequestParseException();
        }
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public RequestDispatcher getRequestDispatcher(String url) {
        return new ApplicationRequestDispatcher(url);
    }

    /**
     * ???????????????????????????JSESSIONID??????Cookie????????????????????????session
     * ??????????????????Session?????????????????????????????????????????????Set-Cookie: JSESSIONID=D5A5C79F3C8E8653BC8B4F0860BFDBCD
     * <p>
     * ?????????????????????????????????Cookie?????????????????????????????????
     * <p>
     * ?????????????????????????????????????????????????????????????????????????????????Set-Cookie??????JSESSIONID=XXXXXXX????????????
     * ???????????????????????????????????????????????????????????????????????????Set-Cookie??????JSESSIONID=XXXXXXX???????????????
     * <p>
     * <p>
     * ???????????????Cookie????????????JSESSIONID???????????????????????????Session?????????????????????????????????Set-Cookie??????JSESSIONID=XXXXXXX???
     * ??????????????????????????????????????????Session???????????????????????????Set-Cookie??????JSESSIONID=XXXXXXX???
     * ??????????????????getSession???????????????????????????Session
     *
     * @return HTTPSession
     */
    public HTTPSession getSession() {
        if (session != null) {
            return session;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getKey().equals("JSESSIONID")) {
                HTTPSession currentSession = servletContext.getSession(cookie.getValue());
                if (currentSession != null) {
                    this.session = currentSession;
                    return session;
                }
            }
        }
        session = servletContext.createSession(requestHandler.getResponse());
        return session;
    }

    private void parseHeaders(String[] lines) {
        log.info("???????????????");
        String firstLine = lines[0];
        //????????????
        String[] firstLineSlices = firstLine.split(CharConstant.BLANK);
        this.method = RequestMethod.valueOf(firstLineSlices[0]);
        log.debug("method:{}", this.method);

        //??????URL
        String rawURL = firstLineSlices[1];
        String[] urlSlices = rawURL.split("\\?");
        this.url = urlSlices[0];
        log.debug("url:{}", this.url);

        //??????URL??????
        if (urlSlices.length > 1) {
            parseParams(urlSlices[1]);
        }
        log.debug("params:{}", this.params);

        //???????????????
        String header;
        this.headers = new HashMap<>();
        for (int i = 1; i < lines.length; i++) {
            header = lines[i];
            if (header.equals("")) {
                break;
            }
            int colonIndex = header.indexOf(':');
            String key = header.substring(0, colonIndex);
            String[] values = header.substring(colonIndex + 2).split(",");
            headers.put(key, Arrays.asList(values));
        }
        log.debug("headers:{}", this.headers);

        //??????Cookie

        if (headers.containsKey("Cookie")) {
            String[] rawCookies = headers.get("Cookie").get(0).split("; ");
            this.cookies = new Cookie[rawCookies.length];
            for (int i = 0; i < rawCookies.length; i++) {
                String[] kv = rawCookies[i].split("=");
                this.cookies[i] = new Cookie(kv[0], kv[1]);
            }
            headers.remove("Cookie");
        } else {
            this.cookies = new Cookie[0];
        }
        log.info("Cookies:{}", Arrays.toString(cookies));
    }

    private void parseBody(String body) {
        log.info("???????????????");
        parseParams(body);
        if (this.params == null) {
            this.params = new HashMap<>();
        }
    }

    private void parseParams(String params) {
        String[] urlParams = params.split("&");
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        for (String param : urlParams) {
            String[] kv = param.split("=");
            String key = kv[0];
            String[] values = kv[1].split(",");
            this.params.put(key, Arrays.asList(values));
        }
    }
}
