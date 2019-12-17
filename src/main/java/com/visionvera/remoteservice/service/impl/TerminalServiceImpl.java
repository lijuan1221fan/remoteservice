package com.visionvera.remoteservice.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.remoteservice.bean.TerminalBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.TerminalDao;
import com.visionvera.remoteservice.service.TerminalService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ClassName: TerminalServiceImpl
 *
 * @author quboka
 * @Description: 终端
 * @date 2018年3月23日
 */
@Service
public class TerminalServiceImpl implements TerminalService {

  private static Logger logger = LoggerFactory.getLogger(TerminalServiceImpl.class);

  @Resource
  private TerminalDao terminalDao;

  // @Resource
  // private JedisClient jedisClient;

  @Resource
  private ServiceCenterDao serviceCenterDao;


  /**
   * @return Map<String   ,   Object>
   * @Description: 查询终端号码
   * @
   * @author quboka
   * @date 2018年3月23日
   */
  @Override
  public List<String> getTerminalList(String serviceKey) {
    return terminalDao.getTerminalList(serviceKey);
  }

  /**
   * @param terminalList
   * @return Map<String   ,   Object>
   * @Description: 终端加入队列
   * @
   * @author quboka
   * @date 2018年3月23日
   */
  @Override
  public Map<String, Object> terminalEnqueue(List<String> terminalList) {
    //添加终端队列
//		Long rpush = jedisClient.rpush(QueueConstant.TERMINAL_QUEUE, terminalList.toArray(new String[0]));
//		logger.info("添加终端队列成功,终端队列长度:"+rpush);
    return ResultUtils.ok("终端加入队列成功");
  }

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
  @Override
  public Map<String, Object> getTerminalListByMap(
      Map<String, Object> paramsMap) {
    Integer pageNum = (Integer) paramsMap.get("pageNum");
    Integer pageSize = (Integer) paramsMap.get("pageSize");

    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }
    List<TerminalBean> terminalList = terminalDao.getTerminalListByMap(paramsMap);
    PageInfo<TerminalBean> list = new PageInfo<TerminalBean>(terminalList);

    return ResultUtils.ok("查询终端列表成功", list);
  }


  /**
   * @param terminalIds 终端id 多个用逗号隔开
   * @param isCoerce 是否强制  0：否 1：强制
   * @return Map<String   ,   Object>
   * @Description: 删除终端
   * @author quboka
   * @date 2018年4月11日
   */
  @Override
  public Map<String, Object> deleteTerminal(Map<String, Object> paramsMap) {
    String terminalIds = (String) paramsMap.get("terminalIds");
    Integer isCoerce = (Integer) paramsMap.get("isCoerce");
    String[] terminalIdArray = terminalIds.split(",");
    if (terminalIdArray == null || terminalIdArray.length == 0) {
      logger.info("请勾选要删除的终端。 ");
      return ResultUtils.ok("请勾选要删除的终端。 ", 0);
    }
    //1.有终端在使用中不允许删
    int isWork = terminalDao.queryWorkStatus(terminalIdArray);
    if (isWork > 0) {
      logger.info("有终端正在使用中，请稍后尝试。 ");
      return ResultUtils.ok("有终端正在使用中，请稍后尝试。", 1);
    }
    //2.有镇终端并有绑定关系 ， 确认删除 解除绑定关系
    if (isCoerce == 0) {
      int isConnection = terminalDao.whetherIsConnection(terminalIdArray);
      if (isConnection > 0) {
        logger.info("有终端与用户存在绑定，是否强制删除? ");
        return ResultUtils.ok("有终端与用户存在绑定，是否强制删除? ", 2);
      }
      //3.确定删除当前选择终端？
      logger.info("请确定是否删除已选终端? ");
      return ResultUtils.ok("请确定是否删除已选终端?", 3);
    }
    //删除终端
    List<String> terminalIdList = Arrays.asList(terminalIdArray);
    int deleteResult = terminalDao.deleteTerminal(terminalIdList);
    if (deleteResult == 0) {
      logger.info("删除终端失败");
      return ResultUtils.error("删除终端失败");
    }
    //查询终端号码
    List<String> numberList = terminalDao.getNumberListById(terminalIdList);
//	    for (String number : numberList) {
//	    	 // 从队列中清除村终端
//		    jedisClient.lrem(QueueConstant.TERMINAL_QUEUE, 0, number);
//		}
    //删除绑定关系
    terminalDao.deleteConnection(terminalIdList);
    logger.info("删除终端成功");
    return ResultUtils.ok("删除终端成功", 4);
  }

  /**
   * @param terminal
   * @return Map<String   ,   Object>
   * @Description: 添加终端
   * @author quboka
   * @date 2018年4月12日
   */
  @Override
  public Map<String, Object> addTerminal(TerminalBean terminal) {
    if (terminal == null || StringUtils.isEmpty(terminal.getAddress())
        || StringUtils.isEmpty(terminal.getTerminalName()) || StringUtils
        .isEmpty(terminal.getNumber())
        || StringUtils.isEmpty(terminal.getIp()) || StringUtils.isEmpty(terminal.getServiceKey())
        || terminal.getType() == null || terminal.getStatus() == null) {

      logger.info("添加终端失败,缺少参数");
      return ResultUtils.error("添加终端失败,缺少参数");
    }
    //判断终端号码是否重复
    Map<String, Object> paramsMap = new HashMap<String, Object>();
    paramsMap.put("number", terminal.getNumber());
    int verifyNumber = terminalDao.checkoutNumber(paramsMap);
    if (verifyNumber > 0) {
      logger.info("添加终端失败,终端号码已存在。");
      return ResultUtils.error("添加终端失败,终端号码已存在。");
    }
    paramsMap.clear();
    //判读终端名称是否重复
    paramsMap.put("terminalName", terminal.getTerminalName());
    int verifyName = terminalDao.checkoutName(paramsMap);
    if (verifyName > 0) {
      logger.info("添加终端失败,终端名称已存在。");
      return ResultUtils.error("添加终端失败,终端名称已存在。");
    }
    paramsMap.clear();
    //添加终端
    int addResult = terminalDao.addTerminal(terminal);
    if (addResult == 0) {
      logger.info("添加终端失败 ");
      return ResultUtils.error("添加终端失败 ");
    }
    //判断是村终端
    int gradeId = serviceCenterDao.getServiceGradeIdByKey(terminal.getServiceKey());
    if (gradeId == 5 && terminal.getStatus() > -1) {
//			if(CommonConstant.SERVICEKEY.equals(terminal.getServiceKey())){
//				//根据是否是村终端，还有状态，是否添加队列中
//				jedisClient.rpush(QueueConstant.TERMINAL_QUEUE, terminal.getNumber());
//			}
    }

    logger.info("添加终端成功 ");
    return ResultUtils.ok("添加终端成功  ");
  }

  /**
   * @param terminal
   * @return Map<String   ,   Object>
   * @Description: 修改终端
   * @author quboka
   * @date 2018年4月12日
   */
  @Override
  public Map<String, Object> updateTerminal(TerminalBean terminal) {
    if (terminal == null || terminal.getTerminalId() == null || terminal.getVersion() == null
        || StringUtils.isEmpty(terminal.getNumber()) || StringUtils.isEmpty(terminal.getIp())
        || StringUtils.isEmpty(terminal.getTerminalName()) || StringUtils
        .isEmpty(terminal.getServiceKey())) {
      logger.info("修改终端失败,缺少参数");
      return ResultUtils.error("修改终端失败,缺少参数");
    }
    //1.终端正在使用中，请稍后尝试，
    int isWork = terminalDao
        .queryWorkStatus(new String[]{String.valueOf(terminal.getTerminalId())});
    if (isWork > 0) {
      logger.info("终端正在使用中，请稍后尝试。 ");
      return ResultUtils.error("终端正在使用中，请稍后尝试。");
    }
    //2.终端与用户存在绑定，请先解除绑定关系。
    int isConnection = terminalDao
        .whetherIsConnection(new String[]{String.valueOf(terminal.getTerminalId())});
    if (isConnection > 0) {
      logger.info("终端与用户存在绑定，请先解除绑定关系");
      return ResultUtils.error("终端与用户存在绑定，请先解除绑定关系");
    }
    //3.如果是村终端修改的时候先从队列中删除，然后修改完以后，如果还是演示村的终端在放回队列
    //判断终端号码是否重复
    Map<String, Object> paramsMap = new HashMap<String, Object>();
    paramsMap.put("number", terminal.getNumber());
    paramsMap.put("terminalId", terminal.getTerminalId());
    int verifyNumber = terminalDao.checkoutNumber(paramsMap);
    if (verifyNumber > 0) {
      logger.info("修改终端失败,终端号码已存在。");
      return ResultUtils.error("修改终端失败,终端号码已存在。");
    }
    paramsMap.clear();
    //判读终端名称是否重复
    paramsMap.put("terminalName", terminal.getTerminalName());
    paramsMap.put("terminalId", terminal.getTerminalId());
    int verifyName = terminalDao.checkoutName(paramsMap);
    if (verifyName > 0) {
      logger.info("修改终端失败,终端名称已存在。");
      return ResultUtils.error("修改终端失败,终端名称已存在。");
    }
    ArrayList<String> terminalIdArr = new ArrayList<String>();
    terminalIdArr.add(String.valueOf(terminal.getTerminalId()));
    List<String> numberListById = terminalDao.getNumberListById(terminalIdArr);
    Long lrem = new Long(0);
//		if(numberListById != null && numberListById.size() >0 ){
//			// 从队列中清除村终端
//			lrem = jedisClient.lrem(QueueConstant.TERMINAL_QUEUE, 0, numberListById.get(0));
//		}
    //修改
    int updateResult = terminalDao.updateTerminal(terminal);
    if (updateResult == 0) {
      logger.info("修改终端失败。");
//    		if(!lrem.equals(0) && numberListById != null && numberListById.size() >0 ){
//    			jedisClient.rpush(QueueConstant.TERMINAL_QUEUE, numberListById.get(0));
//			}
      return ResultUtils.error("修改终端失败。");
    }
    //判断修改后是村还是镇，如果是村属于哪个村，状态是什么。符合规定放回队列
    //判断是村终端
    int gradeId = serviceCenterDao.getServiceGradeIdByKey(terminal.getServiceKey());
    if (gradeId == 5 && terminal.getStatus() > -1) {
      if (CommonConstant.SERVICEKEY.equals(terminal.getServiceKey())) {
        //根据是否是村终端，还有状态，是否添加队列中
        //jedisClient.rpush(QueueConstant.TERMINAL_QUEUE, terminal.getNumber());
      }
    }
    logger.info("修改终端成功。");
    return ResultUtils.ok("修改终端成功。");
  }

}
