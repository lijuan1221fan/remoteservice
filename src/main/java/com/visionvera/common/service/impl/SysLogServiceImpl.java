package com.visionvera.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.common.dao.SysLogDao;
import com.visionvera.common.entity.SysLog;
import com.visionvera.common.service.SysLogService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author dell
 * @ClassName:
 * @Description:
 * @date 2018/10/12
 */
@Service
public class SysLogServiceImpl implements SysLogService {

  private static Logger logger = LoggerFactory.getLogger(SysLogServiceImpl.class);

  /**
   * 分页查询
   *
   * @param paramMap
   * @return
   * @
   */
  @Resource
  private SysLogDao sysLogDao;

  @Override
  public Map<String, Object> getPageLog(Map<String, Object> paramMap) {
    Integer pageNum = (Integer) paramMap.get("pageNum");
    Integer pageSize = (Integer) paramMap.get("pageSize");
    PageHelper.startPage(pageNum, pageSize);
    List<SysLog> list = sysLogDao.queryLog(paramMap);
    PageInfo<SysLog> pageInfo = new PageInfo<SysLog>(list);
    logger.info("日志分页查询成功");
    return ResultUtils.ok("日志分页查询成功", pageInfo);
  }


}
