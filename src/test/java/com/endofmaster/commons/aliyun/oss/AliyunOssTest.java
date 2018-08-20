package com.endofmaster.commons.aliyun.oss;

import com.aliyun.oss.model.OSSObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author YQ.Huang
 */
@Ignore
public class AliyunOssTest {

    private String bucket = "jifenke-private";
    private String endpoint = "oss-cn-beijing.aliyuncs.com";
    private String filePath = "wx-card";
    private String callbackUrl = "https://m.lepluspay.com/upload/image";
    private String keyId = "sUKKLHUWR4Ayjrks";
    private String keySecret = "IuZif8RNp3DeG0CN0hkzvcCtszQ0Jl";
    private AliyunOss aliyunOss = new AliyunOss(bucket, endpoint, keyId, keySecret);

    @Test
    public void download() throws Exception {
        OSSObject stream = aliyunOss.download("cert/wx-server.pfx");
        Assert.assertNotNull(stream);
    }

    @Test
    public void test() {
        UploadCredentials credentials = aliyunOss.buildUploadCredentials(
                filePath, callbackUrl, 5 * 1024 * 1024, 300);
        System.err.println(credentials);
    }

}