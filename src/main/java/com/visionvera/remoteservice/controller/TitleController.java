package com.visionvera.remoteservice.controller;

import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.pojo.TitleVo;
import com.visionvera.remoteservice.service.TitleService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by quboka on 2018/8/22.
 */
@RestController
@RequestMapping("/title")
public class TitleController {

  private static Logger logger = LoggerFactory.getLogger(TitleController.class);

  @Autowired
  private TitleService titleService;

  /**
   * @param [session]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 查询标题
   * @author quboka
   * @date 2018/8/22 14:03
   */
  @RequestMapping("/getTitle")
  public Map<String, Object> getTitle(Integer deptId) {
    if (ShiroUtils.getUserEntity() == null) {
      logger.info("查询标题失败，请先登陆");
      return ResultUtils.error("查询标题失败，请先登陆");
    }
    return titleService.getTitle(ShiroUtils.getUserEntity(), deptId);
  }

  @SysLogAnno("修改标题")
  @RequestMapping("/updateTitle")
  public Map<String, Object> updateTitle(@RequestBody TitleVo titleVo) {

    SysUserBean user = ShiroUtils.getUserEntity();
    if (user == null) {
      logger.info("修改标题失败，请先登陆");
      return ResultUtils.error("修改标题失败，请先登陆");
    }

    return titleService.updateTitle(user, titleVo.getDeptId(), titleVo.getDeptTitle());
  }
}
