package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.StorageResBean;
import com.visionvera.remoteservice.pojo.StorageResVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2019/1/21
 */
public interface StorageResDao {

  int addStorageRes(StorageResVo storageResVo);

  StorageResVo selectStorageByFileId(Integer fileId);

  StorageResVo selectStorageByFileUrl(String fileUrl);

  StorageResVo selectStorageByBusinessId(@Param(value = "businessId") Integer businessId);

  int insertStroageRel(@Param(value = "businessId") Integer businessId,
                         @Param(value = "fileId") Integer fileId);
  int deleteByPrimaryKey(Integer id);
  int deleteByBusinessId(Integer id);
  List<StorageResBean> selectStorageListByBusinessId(@Param(value = "businessId") Integer businessId);
}
