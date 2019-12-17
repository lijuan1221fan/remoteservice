package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.X86ConfigInformationBean;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 * 功能描述:X86配置信息
 *
 * @ClassName: X86ConfigInformationService
 * @Author: ljfan
 * @Date: 2019-06-27 11:13
 * @Version: V1.0
 */
public interface X86ConfigInformationService {
  /**
   * 功能描述:创建X86配置信息
   *
   * @ClassName: X86ConfigInformationService
   * @Author: ljfan
   * @Date: 2019-06-27 11:13
   * @Version: V1.0
   */
 /* Map<String, Object> creatX86ConfigInformation(
      X86ConfigInformationBean x86ConfigInformationBean);*/
  /**
   * 功能描述:更新X86配置信息
   *
   * @ClassName: X86ConfigInformationService
   * @Author: ljfan
   * @Date: 2019-06-27 11:13
   * @Version: V1.0
   */
  Map<String, Object> updateX86ConfigInformation(
      List<X86ConfigInformationBean> x86ConfigInformationBeanList);
  /**
   * 功能描述:获取X86配置信息
   *
   * @ClassName: X86ConfigInformationService
   * @Author: ljfan
   * @Date: 2019-06-27 11:13
   * @Version: V1.0
   */
  Map<String, Object> getX86ConfigInformation();
  /**
   * 功能描述:上传X86更新zip包
   *
   * @ClassName: X86ConfigInformationService
   * @Author: ljfan
   * @Date: 2019-06-27 11:13
   * @Version: V1.0
   */
  Map<String, Object> uploadX86zip(MultipartFile file);
}
