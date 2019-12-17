package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.NumberIteration;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.pojo.DeptListVo;
import java.util.List;
import java.util.Map;

/**
 * @Auther: quboka
 * @Date: 2018/8/28 18:16
 * @Description:
 */
public interface NumberService {

  /**
   * @param DeptListVo
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 通过服务中心key查询部门列表
   * @author quboka
   * @date 2018/8/28 18:22
   */
  Map<String, Object> getDeptList(DeptListVo deptListVo);

  /**
   * @param serviceKey 村key
   * @param typeId 业务详情id
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 获取业务号码
   * @author quboka
   * @date 2018/8/29 11:05
   */
  Map<String, Object> takeBusinessNumberAndModify(String serviceKey, Integer businessId,Integer type,Integer appointmentId,String androidBusinessType);

  /**
   * @param serviceKey 村key
   * @param typeId 业务详情id
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 新版叫号机获取业务号码
   * @author quboka
   * @date 2018/8/29 11:05
   */
  Map<String, Object> getBusinessNumberAndModify(String serviceKey, Integer businessId,Integer type,Integer appointmentId,String name,String idCard);


  /**
   * 根据审批中心及部门查询迭代号码
   * @param numberIteration
   * @return
   */
  NumberIteration getNumberIteration(Integer deptId,String serviceKey);

  /**
   * 根据审批中心获取在线业务员
   *
   * @param serviceKey
   * @return
   */
  default List<SysUserBean> getOnLineUserForWork(List<String> serviceKey) {
    return null;
  }

  /**
   * 根据村中心serviceKey取得审批中心等待业务人员，并推号
   */
  public void sendMessageForWorkingByServiceKey(String serviceKey,Integer deptId);

  /**
   * 给在线业务员及叫号机同步待办号
   * @param serviceKey
   * @param deptId
   * @param androidBusinessType
   */
  void sendMessage(String serviceKey, Integer deptId, String androidBusinessType) ;


  /**
   * 新版叫号机给在线业务员及叫号机同步待办号
   * @param serviceKey
   * @param deptId
   */
  void sendMessageAll(String serviceKey, Integer deptId) ;

  Map<String,Object> searchBusinessInfo(String idCardNumber,String serviceKey);
  void sendMessageToH5(String serviceKey,Integer deptId);
}
