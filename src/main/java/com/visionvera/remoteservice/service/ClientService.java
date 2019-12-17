package com.visionvera.remoteservice.service;

import java.util.Map;

/**
 * ClassName: ClientService
 *
 * @author quboka
 * @Description: 客户信息
 * @date 2018年4月10日
 */
public interface ClientService {

  /**
   * @return Map<String   ,   Object>
   * @Description: 查询民族
   * @author quboka
   * @date 2018年4月10日
   */
  Map<String, Object> getNationalList();

//	/**
//	 * @Description: 添加或者修改客户信息
//	 * @param clientInfo
//	 * @return
//	 * @return Map<String,Object>
//	 * @author quboka
//	 * @date 2018年4月10日
//	 */
//	Map<String, Object> addOrUpdateClientInfo(ClientInfo clientInfo);

}
