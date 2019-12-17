package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.TerminalBean;
import java.util.List;
import java.util.Map;

/**
 * ClassName: TerminalDao
 *
 * @author quboka
 * @Description: 终端
 * @date 2018年3月23日
 */
public interface TerminalDao {

  /**
   * @return Map<String       ,       Object>
   * @Description: 查询终端号码
   * @
   * @author quboka
   * @date 2018年3月23日
   */
  List<String> getTerminalList(String serviceKey);

  /**
   * @param terminalMap
   * @return int
   * @Description: 根据终端号码修改终端状态
   * @author quboka
   * @date 2018年3月24日
   */
  int modifyTerminalStatusByNumber(Map<String, Object> terminalMap);

  /**
   * @param userId
   * @return List<TerminalBean>
   * @Description: 用户对应终端
   * @author quboka
   * @date 2018年3月24日
   */
  List<TerminalBean> getTerminalListbyUserId(Integer userId);

  /**
   * @param number 终端号码
   * @return TerminalBean
   * @Description: 根据终端号查询终端
   * @author quboka
   * @date 2018年3月27日
   */
  TerminalBean getTerminalByNumber(String number);

  /**
   * @param pageNum
   * @param pageSize
   * @param terminalId 终端id
   * @param terminalName 终端名称
   * @param number 终端号码
   * @param serviceKey 所属服务中心key
   * @return Map<String       ,       Object>
   * @Description: 获取终端列表
   * @author quboka
   * @date 2018年4月11日
   */
  List<TerminalBean> getTerminalListByMap(Map<String, Object> paramsMap);

  /**
   * @param paramsMap
   * @return String[]
   * @Description:按状态筛选
   * @author quboka
   * @date 2018年4月11日
   */
  List<Integer> filtrateByState(Map<String, Object> paramsMap);

  /**
   * @param terminalIdList
   * @return int
   * @Description: 删除终端
   * @author quboka
   * @date 2018年4月11日
   */
  int deleteTerminal(List<String> terminalIdList);

  /**
   * @param terminalIdList
   * @return List<String>
   * @Description: 根据终端id 查询终端号码
   * @author quboka
   * @date 2018年4月11日
   */
  List<String> getNumberListById(List<String> terminalIdList);

  /**
   * @param terminalIdArray
   * @return int
   * @Description: 查询工作状态
   * @author quboka
   * @date 2018年4月12日
   */
  int queryWorkStatus(String[] terminalIdArray);

  /**
   * @param terminalIdArray
   * @return int
   * @Description: 是否存在关联
   * @author quboka
   * @date 2018年4月12日
   */
  int whetherIsConnection(String[] terminalIdArray);

  /**
   * @param terminalIdList
   * @return void
   * @Description: 删除关联
   * @author quboka
   * @date 2018年4月12日
   */
  void deleteConnection(List<String> terminalIdList);

  /**
   * @param paramsMap
   * @return int
   * @Description: 判断终端号码是否重复
   * @author quboka
   * @date 2018年4月12日
   */
  int checkoutNumber(Map<String, Object> paramsMap);

  /**
   * @param paramsMap
   * @return int
   * @Description: 判读终端名称是否重复
   * @author quboka
   * @date 2018年4月12日
   */
  int checkoutName(Map<String, Object> paramsMap);

  /**
   * @param terminal
   * @return int
   * @Description: 添加终端
   * @author quboka
   * @date 2018年4月12日
   */
  int addTerminal(TerminalBean terminal);

  /**
   * @param terminal
   * @return int
   * @Description: 修改终端
   * @author quboka
   * @date 2018年4月12日
   */
  int updateTerminal(TerminalBean terminal);
}
