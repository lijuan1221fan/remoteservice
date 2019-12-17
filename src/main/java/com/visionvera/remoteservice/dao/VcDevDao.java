package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.VcDevBean;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: VcDevDao
 *
 * @author quboka
 * @Description: 会管终端
 * @date 2018年5月17日
 */
public interface VcDevDao {

  /**
   * @return Integer
   * @Description: 是否有终端在工作中
   * @author quboka
   * @date 2018年5月17日
   */
  Integer isDeviceWorking();

  /**
   * @return List<VcDevBean>
   * @Description: 查询终端列表
   * @author quboka
   * @date 2018年5月17日
   */
  List<VcDevBean> getDeviceList();

  /**
   * @param deleteByIdList
   * @return void
   * @Description: 删除终端
   * @author quboka
   * @date 2018年5月17日
   */
  void deleteDevice(List<Integer> deleteByIdList);

  /**
   * @param addList
   * @return void
   * @Description: 添加终端
   * @author quboka
   * @date 2018年5月17日
   */
  void addDeviceList(List<VcDevBean> addList);

  /**
   * @param form
   * @return List<String>
   * @Description: 根据形态查询 终端id（终端号）
   * @author quboka
   * @date 2018年5月18日
   */
  List<String> getDeviceIdListByForm(Integer form);

  /**
   * @param userId
   * @return VcDevBean
   * @Description: 根据用户查询对应的终端
   * @author quboka
   * @date 2018年5月18日
   */
  VcDevBean getDeviceByUserId(Integer userId);

  /**
   * @param deviceMap
   * @return int
   * @Description: 修改终端本地属性
   * @author quboka
   * @date 2018年5月18日
   */
  int updateVcDecvice(Map<String, Object> deviceMap);

  /**
   * @param id
   * @return VcDevBean
   * @Description: 根据id 获取终端
   * @author quboka
   * @date 2018年5月18日
   */
  VcDevBean getDeviceById(String id);

  /**
   * @param id
   * @return Integer
   * @Description: 解绑终端
   * @author quboka
   * @date 2018年5月21日
   */
  Integer unbindDeviceAndModify(String id);

  /**
   * @param serviceId
   * @return List<VcDevBean>
   * @Description: 根据服务中心key查询终端列表
   * @author quboka
   * @date 2018年5月21日
   */
  List<VcDevBean> getDeviceByServiceKey(String serviceKey);

  /**
   * 根据中心key和部门id 查询终端
   *
   * @param paramMap
   * @return
   */
  VcDevBean getDeviceByServiceKeyAndDeptId(Map paramMap);


  /**
   * @param pageNum
   * @param pageSize
   * @param id 终端id 终端号码
   * @param name 终端名称
   * @param serviceKey 服务中心名称
   * @return List<VcDevBean>
   * @Description: 查询终端列表
   * @author quboka
   * @date 2018年5月21日
   */
  List<VcDevBean> getDeviceListByMap(Map<String, Object> paramsMap);

  /**
   * @param serviceKey 服务中心key
   * @return List<VcDevBean>
   * @Description: 根据村key查询可用终端
   * @author quboka
   * @date 2018年5月24日
   */
  List<VcDevBean> getAvailableDevice(String serviceKey);

  /**
   * @return void
   * @Description: 重置状态
   * @author quboka
   * @date 2018年5月29日
   */
  void resetTheState();

  /**
   * @param form 形态 0：未分配 1：村终端 2：镇终端 3：默认终端 4:高拍仪终端
   * @param associated 关联字段。用于扩展关联，当form为4时此字段为高拍仪扩展字段
   * @return VcDevBean
   * @Description: 查寻关联终端
   * @author quboka
   * @date 2018年6月13日
   */
  VcDevBean getAssociatedDev(@Param("form") int form,
      @Param("associated") String associated);


  /**
   * @param ids
   * @return List<Integer>
   * @Description:查询用户对应得终端id
   * @author quboka
   * @date 2018年7月4日
   */
  List<Integer> getIdsByUsers(Integer[] ids);

  /**
   * @param devIdList
   * @return List<Integer>
   * @Description: 查询终端工作状态
   * @author quboka
   * @date 2018年7月4日
   */
  List<Integer> getStateByIds(@Param("devIdList") List<Integer> devIdList);

  /**
   * @param devIdList
   * @return Integer
   * @Description: 解绑终端(多个)
   * @author quboka
   * @date 2018年7月4日
   */
  Integer unbindDeviceList(List<Integer> devIdList);

  /**
   * @param serviceKeys
   * @return List<Integer>
   * @Description: 根据服务中心key 查询终端id
   * @author quboka
   * @date 2018年7月4日
   */
  List<String> getIdsByServices(@Param("serviceKeys") List<String> serviceKeys);

  /**
   * @param windowId
   * @return VcDevBean
   * @Description: 根据窗口查询对应的终端
   * @author jlm
   * @date 2018年11月5日
   */
  VcDevBean getDeviceByWindowId(Integer windowId);

  /**
   * @param paramsMap
   * @Description: 超管查询终端
   * @author jlm
   * @date 2018年11月5日
   */
  List<VcDevBean> superGetDevList(Map<String, Object> paramsMap);

  /**
   * 根据ip查询绑定终端
   *
   * @param ip
   * @return
   */
  VcDevBean getDeviceByIP(String ip);

  /**
   * 修改终端信息
   *
   * @param deviceId
   * @param ip
   * @param type
   * @return
   */
  int updateDeviceDetail(String deviceId, String ip, Integer type);

  /**
   * 将终端置为空闲状态
   *
   * @param deviceId
   * @return
   */
  int setIdle(String deviceId);

  List<VcDevBean> getDeviceListByServiceKeyAndDeptId(Map map);

    Integer deleteAll();

  List<String> selectAllDeviceId();


}
