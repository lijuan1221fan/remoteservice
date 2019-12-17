package com.visionvera.remoteservice.service.impl;

import com.visionvera.remoteservice.bean.CommonConfigBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.dao.CommonConfigDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.service.TitleService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.visionvera.remoteservice.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by quboka on 2018/8/22.
 */
@Service
public class TitleServiceImpl implements TitleService {

  @Autowired
  private ServiceCenterDao serviceCenterDao;

  @Autowired
  private SysDeptBeanDao sysDeptBeanDao;

  @Autowired
  private CommonConfigDao commonConfigDao;


  /**
   * @param [user]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 查询标题
   * @author quboka
   * @date 2018/8/22 14:08
   */
  @Override
  public Map<String, Object> getTitle(SysUserBean user, Integer deptId) {
    HashMap<String, Object> result = new HashMap<>();
    List<String> deptTitle = null;
    deptTitle = sysDeptBeanDao.getDeptTitle(deptId);
    //全部部门的用户，二级标题查询common_config表
    if(user.getDeptId() == 0){
      CommonConfigBean title = commonConfigDao.getCommonConfigByName("socialSubhead");
      if(title != null){
        deptTitle.add(title.getCommonValue());
      }
    }
    result.put("deptTitle", deptTitle);
    return ResultUtils.ok("获取标题成功", result);
  }

  /**
   * @param [user, deptId]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改标题
   * @author quboka
   * @date 2018/8/22 15:41
   */
  @Override
  public Map<String, Object> updateTitle(SysUserBean user, Integer deptId, String deptTitle) {
    if(StringUtil.isNotNull(deptTitle)){
      if(deptId == null){
        return ResultUtils.error("部门ID不能为空");
      }
    }
    if(StringUtil.isNotNull(deptTitle) && deptId != null && !deptId.equals(0)){
      int i = sysDeptBeanDao.updateServiceTitle(deptTitle, deptId);

      if(i != 1){
        return ResultUtils.error("修改标题失败");
      }

      return ResultUtils.ok("修改标题成功");
    }
    return ResultUtils.ok("修改标题成功");
  }
}
