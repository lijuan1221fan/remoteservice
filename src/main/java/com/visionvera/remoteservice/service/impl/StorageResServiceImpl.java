package com.visionvera.remoteservice.service.impl;

import com.visionvera.remoteservice.dao.StorageResDao;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.service.StorageResService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author dell
 * @ClassName:
 * @Description:
 * @date 2019/1/24
 */
@Service
public class StorageResServiceImpl implements StorageResService {

  @Resource
  private StorageResDao storageResDao;

  @Override
  public int addStorageRes(StorageResVo storageResVo) {
    return storageResDao.addStorageRes(storageResVo);
  }

  @Override
  public StorageResVo selectStorageByFileId(Integer fileId) {
    return storageResDao.selectStorageByFileId(fileId);
  }

  @Override
  public StorageResVo selectStorageByFileUrl(String fileUrl) {
    return storageResDao.selectStorageByFileUrl(fileUrl);
  }
}
