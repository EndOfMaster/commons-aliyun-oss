package com.endofmaster.commons.aliyun.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.model.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.*;

/**
 * @author YQ.Huang
 * @update ZM.Wang
 */
public class AliyunOss {

    private final static Logger logger = LoggerFactory.getLogger(AliyunOss.class);

    private final String bucket;
    private final String endpoint;
    private final String accessKeyId;
    private final String accessKeySecret;
    private final String bucketUrl;
    private final OSSClient ossClient;

    public AliyunOss(String bucket, String endpoint, String accessKeyId, String accessKeySecret) {
        this.bucket = bucket;
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketUrl = "https://" + bucket + "." + endpoint;
        this.ossClient = ossClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 构建上传凭证
     *
     * @param folder           文件夹，null表示根目录
     * @param callbackUrl      回调Url，null表示不用回调
     * @param maxContentLength 允许上传的最大文件大小
     * @param expirationSecs   凭证有效期，凭证过期后无效，需要重新请求构建
     * @return 上传凭证
     */
    public UploadCredentials buildUploadCredentials(String folder, String callbackUrl, int maxContentLength, int expirationSecs) {
        PolicyConditions policyConditions = new PolicyConditions();
        // 前缀
        String prefix = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + "-";
        if (folder != null) {
            prefix = folder + "/" + prefix;
        }
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, prefix);
        // 文件大小限制
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxContentLength);
        // 链接有效期
        Date expiration = Date.from(Instant.now().plusSeconds(expirationSecs));
        String policy = ossClient.generatePostPolicy(expiration, policyConditions);
        String signature = ossClient.calculatePostSignature(policy);
        // 回调
        String callback = null;
        if (callbackUrl != null) {
            String callbackJson = OSSUtils.jsonizeCallback(buildUploadCallback(callbackUrl));
            callback = Base64.getEncoder().encodeToString(callbackJson.getBytes());
        }
        return new UploadCredentials(
                bucketUrl,
                accessKeyId,
                Base64.getEncoder().encodeToString(policy.getBytes()),
                signature,
                "200",
                prefix,
                callback);
    }

    /**
     * 查询是否存在这个文件
     */
    public boolean has(String key) {
        return ossClient.doesObjectExist(bucket, key);
    }

    /**
     * 查询文件类型
     */
    public String queryType(String key) {
        ObjectMetadata metadata = ossClient.getObjectMetadata(bucket, key);
        return metadata.getContentType();
    }

    /**
     * 下载文件
     *
     * @param key OSS对象
     * @return 阿里云数据对象
     */
    public OSSObject download(String key) {
        return ossClient.getObject(bucket, key);
    }

    /**
     * 上传文件
     */
    public void upload(String key, InputStream input) {
        ossClient.putObject(bucket, key, input);
    }

    /**
     * 构建某个OSS对象的下载链接
     *
     * @param key            OSS对象
     * @param expirationSecs 链接有效期
     * @return 下载链接
     */
    public String buildDownloadUrl(String key, int expirationSecs) {
        Date expiration = Date.from(Instant.now().plusSeconds(expirationSecs));
        URL url = ossClient.generatePresignedUrl(bucket, key, expiration, HttpMethod.GET);
        return url.toString();
    }

    /**
     * 构建上传回调响应
     */
    public Map<String, Object> buildUploadResponse(String key, Integer size, String mimeType, boolean needDownloadUrl) {
        Map<String, Object> result = new HashMap<>();
        if (needDownloadUrl) {
            result.put("url", buildDownloadUrl(key, 100));
        } else {
            result.put("url", key);
        }
        result.put("size", size);
        result.put("mimeType", mimeType);
        result.put("Status","Ok");
        return result;
    }

    /**
     * 删除单个文件
     */
    public void delete(String key) {
        ossClient.deleteObject(bucket, key);
    }

    /**
     * 删除多个文件
     */
    public void delete(List<String> keys) {
        if (keys != null && keys.size() > 0) {
            DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(keys));
            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
            if (deletedObjects != null && deletedObjects.size() > 0) {
                deletedObjects.forEach(key -> logger.debug("批量删除失败的key：" + key));
            }
        }
    }


    private Callback buildUploadCallback(String callbackUrl) {
        Callback callback = new Callback();
        callback.setCallbackUrl(callbackUrl);
        String body = "bucket=${bucket}&" +
                "ossKey=${object}&" +
                "size=${size}&" +
                "mimeType=${mimeType}";
        callback.setCallbackBody(body);
        callback.setCalbackBodyType(Callback.CalbackBodyType.URL);
        return callback;
    }

    private OSSClient ossClient(String endpoint, String accessKeyId, String accessKeySecret) {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setProtocol(Protocol.HTTPS);
        return new OSSClient(endpoint, accessKeyId, accessKeySecret, conf);
    }
}
