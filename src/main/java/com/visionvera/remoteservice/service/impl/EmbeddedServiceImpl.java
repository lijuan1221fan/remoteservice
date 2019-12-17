package com.visionvera.remoteservice.service.impl;

import com.visionvera.remoteservice.bean.EmbeddedServerInfo;
import com.visionvera.remoteservice.dao.EmbeddedServerDao;
import com.visionvera.remoteservice.service.EmbeddedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;

@Service
public class EmbeddedServiceImpl implements EmbeddedService {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedServiceImpl.class);
    @Resource
    private EmbeddedServerDao embeddedServerDao;

    /**
     * 删除createTime超过两分钟的关联关系数据
     */
    @Override
    public void deleteFailureConnection() {
        //查询createTime超过两分钟没更新的关联关系
        List<EmbeddedServerInfo> list = embeddedServerDao.getValidEmbeddedServer();
        if (!CollectionUtils.isEmpty(list)) {
            for (EmbeddedServerInfo embeddedServer : list) {
                embeddedServerDao.deleteEmbeddedServer(embeddedServer.getSessionId());
                logger.info("删除sessionId为" + embeddedServer.getSessionId() + "的关联关系");
            }
        }
    }

}
