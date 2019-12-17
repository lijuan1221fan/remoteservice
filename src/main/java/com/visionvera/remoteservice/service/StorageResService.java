package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.pojo.StorageResVo;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2019/1/24
 */
public interface StorageResService {

  int addStorageRes(StorageResVo storageResVo);

  StorageResVo selectStorageByFileId(Integer fileId);

  StorageResVo selectStorageByFileUrl(String fileUrl);

}
