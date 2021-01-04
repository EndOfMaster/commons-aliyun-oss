package com.endofmaster.commons.aliyun.oss;

import com.aliyun.oss.model.OSSObjectSummary;

/**
 * @author ZM.Wang
 */
public class OssObject {

    private final String key;

    private final long size;

    private final String type;

    public OssObject(OSSObjectSummary summary) {
        this.key = summary.getKey();
        this.size = summary.getSize();
        this.type = summary.getType();
    }

    public String getKey() {
        return key;
    }

    public long getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
