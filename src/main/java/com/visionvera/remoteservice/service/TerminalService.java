package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.TerminalBean;
import java.util.List;
import java.util.Map;


/**
 * ClassName: TerminalService
 *
 * @author quboka
 * @Description: 终端
 * @date 2018年3月23日
 */
public interface TerminalService {

  /**
   * @return Map<String   ,   Object>
   * @Description: 查询终端号码
   * @
   * @author quboka
   * @date 2018年3月23日
   */
  List<String> getTerminalList(String serviceKey);

  /**
   * @param terminalList
   * @return Map<String   ,   Object>
   * @Description: 终端加入队列
   * @
   * @author quboka
   * @date 2018年3月23日
   */
  Map<String, Object> terminalEnqueue(List<String> terminalList);


  /**
   * @param pageNum
   * @param pageSize
   * @param terminalId 终端id
   * @param terminalName 终端名称
   * @param number 终端号码
   * @param serviceKey 所属服务中心key
   * @return Map<String   ,   Object>
   * @Description: 获取终端列表
   * @author quboka
   * @date 2018年4月11日
   */
  Map<String, Object> getTerminalListByMap(
      Map<String, Object> paramsMap);

  /**
   * @param terminalIds 终端id 多个用逗号隔开
   * @param isCoerce 是否强制  0：否 1：强制
   * @return Map<String   ,   Object>
   * @Description: 删除终端
   * @author quboka
   * @date 2018年4月11日
   */
  Map<String, Object> deleteTerminal(Map<String, Object> paramsMap);

  /**
   * @param terminal
   * @return Map<String   ,   Object>
   * @Description: 添加终端
   * @author quboka
   * @date 2018年4月12日
   */
  Map<String, Object> addTerminal(TerminalBean terminal);

  /**
   * @param terminal
   * @return Map<String   ,   Object>
   * @Description: 修改终端
   * @author quboka
   * @date 2018年4月12日
   */
  Map<String, Object> updateTerminal(TerminalBean terminal);

}
