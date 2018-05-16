package com.endofmaster.commons.aliyun.oss;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author YQ.Huang
 */
public class AliyunOssUtilsTest {

    @Test
    public void getOssKey() throws Exception {
        String url = "https://payingcloud-upload-test.oss-cn-beijing.aliyuncs.com/1%40qq.com/20170124121533911-zhou.png?Expires=1485233367&OSSAccessKeyId=LTAIpn1rcc60P6fm&Signature=bDYZJSaCd5bykRO7fc1u2WAYKbA%3D";
        String key = AliyunOssUtils.getOssKey(url);
        Assert.assertEquals("1@qq.com/20170124121533911-zhou.png", key);
    }

    @Test
    public void getOssKey_notUrl() throws Exception {
        String url = "1@qq.com/20170124121533911-zhou.png";
        String key = AliyunOssUtils.getOssKey(url);
        Assert.assertEquals("1@qq.com/20170124121533911-zhou.png", key);
    }
}