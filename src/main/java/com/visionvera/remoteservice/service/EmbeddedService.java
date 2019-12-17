package com.visionvera.remoteservice.service;

public interface EmbeddedService {

    /**
     * 删除embeddedService表中的失效的关联关系
     * 失效条件：连接中的创建时间字段中的时间超过两分钟
     */
    void deleteFailureConnection();
}
