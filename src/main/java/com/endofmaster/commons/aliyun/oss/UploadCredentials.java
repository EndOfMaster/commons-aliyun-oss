package com.endofmaster.commons.aliyun.oss;

/**
 * @author YQ.Huang
 * @update ZM.Wang
 */
public class UploadCredentials {
    private final String url;
    private final String ossAccessKeyId;
    private final String policy;
    private final String signature;
    private final String successActionStatus;
    private final String prefix;
    private final String callback;

    public UploadCredentials(String url, String ossAccessKeyId, String policy, String signature,
                             String successActionStatus, String prefix, String callback) {
        this.url = url;
        this.ossAccessKeyId = ossAccessKeyId;
        this.policy = policy;
        this.signature = signature;
        this.successActionStatus = successActionStatus;
        this.prefix = prefix;
        this.callback = callback;
    }

    public String getUrl() {
        return url;
    }

    public String getOssAccessKeyId() {
        return ossAccessKeyId;
    }

    public String getPolicy() {
        return policy;
    }

    public String getSignature() {
        return signature;
    }

    public String getSuccessActionStatus() {
        return successActionStatus;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCallback() {
        return callback;
    }

    @Override
    public String toString() {
        return "UploadCredentials{" +
                "url='" + url + '\'' +
                ", ossAccessKeyId='" + ossAccessKeyId + '\'' +
                ", policy='" + policy + '\'' +
                ", signature='" + signature + '\'' +
                ", successActionStatus='" + successActionStatus + '\'' +
                ", prefix='" + prefix + '\'' +
                ", callback='" + callback + '\'' +
                '}';
    }
}
