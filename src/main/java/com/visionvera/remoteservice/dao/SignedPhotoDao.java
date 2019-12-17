package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.SignedPhotoBean;
import java.util.List;
import java.util.Map;

/**
 * ClassName: SignedPhotoDao
 *
 * @author quboka
 * @Description: 数字签名照
 * @date 2018年4月30日
 */
public interface SignedPhotoDao {

  /**
   * @param signedPhotoBean
   * @return int
   * @Description: 插入数字签名照片
   * @author quboka
   * @date 2018年4月30日
   */
  int addSignedPhoto(SignedPhotoBean signedPhotoBean);

  /**
   * @param paramsMap
   * @return int
   * @Description: 删除签名照片
   * @author quboka
   * @date 2018年5月1日
   */
  int deleteSignedPhoto(Map<String, Object> paramsMap);

  /**
   * @param paramsMap
   * @return List<SignedPhotoBean>
   * @Description: 查看签名照列表
   * @author quboka
   * @date 2018年5月1日
   */
  List<SignedPhotoBean> getSignedPhotoList(
      Map<String, Object> paramsMap);

}
