package com.jifenke.commons.aliyun.oss;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * @author YQ.Huang
 * @update ZM.Wang
 */
public abstract class AliyunOssUtils {

    public static String getOssKey(String encodedUrl) {
        try {
            String decodedUrl = URLDecoder.decode(encodedUrl, "UTF-8");
            URL url = new URL(decodedUrl);
            return url.getPath().substring(1);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            return encodedUrl;
        }
    }
}
