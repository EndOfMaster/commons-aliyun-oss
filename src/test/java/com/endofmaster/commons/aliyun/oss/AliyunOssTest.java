package com.endofmaster.commons.aliyun.oss;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;


/**
 * @author YQ.Huang
 */
@Disabled
public class AliyunOssTest {

    private final String bucket = "jifenke-private";
    private final String endpoint = "oss-cn-beijing.aliyuncs.com";
    private final String filePath = "wx-card";
    private final String callbackUrl = "https://m.lepluspay.com/upload/image";
    private final String keyId = "******";
    private final String keySecret = "*********";
    private final AliyunOss aliyunOss = new AliyunOss(bucket, endpoint, keyId, keySecret);

    @Test
    public void download() {
        InputStream stream = aliyunOss.download("cert/wx-server.pfx");
        Assertions.assertNotNull(stream);
    }

    @Test
    public void test() {
        UploadCredentials credentials = aliyunOss.buildUploadCredentials(
                filePath, callbackUrl, 5 * 1024 * 1024, 300);
        System.err.println(credentials);
    }

}