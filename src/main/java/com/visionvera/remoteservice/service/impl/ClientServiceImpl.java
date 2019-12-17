package com.visionvera.remoteservice.service.impl;

import com.visionvera.remoteservice.dao.ClientInfoDao;
import com.visionvera.remoteservice.service.ClientService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ClassName: ClientServiceImpl
 *
 * @author quboka
 * @Description: 客户信息
 * @date 2018年4月10日
 */
@Service
public class ClientServiceImpl implements ClientService {

  private static Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

  @Resource
  private ClientInfoDao clientInfoDao;

  /**
   * @return Map<String   ,   Object>
   * @Description: 查询民族
   * @author quboka
   * @date 2018年4月10日
   */
  @Override
  public Map<String, Object> getNationalList() {
//		List<DictionaryBean> nationalList = clientInfoDao.getNationalList();
//		logger.info("名族列表:" + nationalList);
    return ResultUtils.ok("查询民族成功");
  }

//	/**
//	 * @Description: 添加或者修改客户信息
//	 * @param clientInfo
//	 * @return
//	 * @return Map<String,Object>
//	 * @author quboka
//	 * @date 2018年4月10日
//	 */
//	@Override
//	public Map<String, Object> addOrUpdateClientInfo(ClientInfo clientInfo) {
//		String applicantName = clientInfo.getApplicantName();
//		String idNumber = clientInfo.getIDNumber();
//		if(StringUtils.isEmpty(idNumber) || StringUtils.isEmpty(applicantName) ){
//			logger.info("添加或者修改客户信息失败,身份证号和姓名不能为空。");
//			return ResultUtils.error("添加或者修改客户信息失败,身份证号和姓名不能为空。");
//		}
//		int addClientInfo = clientInfoDao.addClientInfo(clientInfo);
//		if(addClientInfo == 0){
//			logger.info("添加或者修改客户信息失败。");
//			return ResultUtils.error("添加或者修改客户信息失败。");
//		}
//		logger.info("添加或者修改客户信息成功。");
//		return ResultUtils.ok("添加或者修改客户信息成功。");
//	}

}
