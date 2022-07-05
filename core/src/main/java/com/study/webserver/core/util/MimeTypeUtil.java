package com.study.webserver.core.util;

import com.study.webserver.core.constant.Context;
import eu.medsea.mimeutil.MimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class MimeTypeUtil {
    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    public static String getTypes(String fileName) {
        if(fileName.endsWith(".html")){
            return Context.DEFAULT_CONTENT_TYPE;
        }
        Collection mimeTypes = MimeUtil.getMimeTypes(MimeTypeUtil.class.getResource(fileName));
        return mimeTypes.toArray()[0].toString();
    }

}
