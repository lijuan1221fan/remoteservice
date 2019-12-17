package com.visionvera.remoteservice.controller;

import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.validator.util.AssertUtil;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.CommonConfigBean;
import com.visionvera.remoteservice.request.MeetingConfigRequest;
import com.visionvera.remoteservice.service.CommonConfigService;
import com.visionvera.remoteservice.util.ResultUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 * 通用配置控制层
 * @author wangrz
 * @date 2017年12月21日16:27:03
 *
 */
@RestController
@RequestMapping("commonConfig")
public class CommonConfigController {

  @Autowired
  private CommonConfigService commonConfigService;

  /***
   * 查询指定值-通用
   * @param commonName 指定名称
   * @return
   */
  @RequestMapping("findCommonValue")
  public Map<String, Object> findCommonValue(String commonName) {
    List<CommonConfigBean> commonConfigByName = commonConfigService
        .findCommonConfigByName(commonName);
    return ResultUtils.ok("查询成功", commonConfigByName);
  }

  /***
   * 查询指定值-通用
   * @param commonName 指定名称
   * @return
   */
  @RequestMapping("commonValue")
  public Map<String, Object> getCommonValueByCommonName(String commonName) {
    CommonConfigBean commonConfigByName = commonConfigService.getCommonConfigByName(commonName);
    return ResultUtils.ok("查询成功", commonConfigByName);

  }

  @SysLogAnno("增加通用值")
  @RequestMapping("add")
  public Map<String, Object> add(
      @RequestParam(value = "commonName", required = true) String commonName,
      @RequestParam(value = "commonValue", required = true) String commonValue) {
    Map<String, Object> paramsMap = new HashMap<String, Object>();
    paramsMap.put("commonName", commonName);
    paramsMap.put("commonValue", commonValue);
    return commonConfigService.add(paramsMap);

  }

  //删
  @SysLogAnno("删除通用值")
  @RequestMapping("delete")
  public Map<String, Object> delete(
      @RequestParam(value = "id", required = true) Integer id) {
    return commonConfigService.delete(id);
  }

  //改
  @SysLogAnno("修改通用值")
  @RequiresPermissions("sys:save")
  @RequestMapping("update")
  public Map<String, Object> update(CommonConfigBean commonConfig) {
    return commonConfigService.update(commonConfig);
  }

  /**
   * @return Map<String   ,   Object>
   * @Description: 批量修改
   * @author quboka
   * @date 2018年1月3日
   */
  @SysLogAnno("批量修改通用值")
  @RequestMapping("updateAll")
  public Map<String, Object> updateAll(@RequestBody Map<String, Object> paramsMap) {
    return commonConfigService.updateAll(paramsMap);
  }

  /**
   * @return Map<String   ,   Object>
   * @Description: 查询列表
   * @author quboka
   * @date 2018年1月3日
   */
  @RequestMapping("getList")
  public Map<String, Object> getList() {
    return commonConfigService.getList();
  }

  /**
   * @return Map<String   ,   Object>
   * @Description: 会管信息验证
   * @author lijintao
   * @date 2018年7月5日
   */
  @RequestMapping("checkMeetingConfig")
  public Map<String, Object> checkMeetingConfig(
      @RequestBody MeetingConfigRequest meetingConfigRequest) {
    ValidateUtil.validate(meetingConfigRequest);
    return commonConfigService.checkMeetingConfig(meetingConfigRequest);
  }

    @GetMapping("changeVersion")
    public Map<String, Object> changeVersion(Integer version) {
        AssertUtil.isEmpty(version, "参数不正确");
        return commonConfigService.changeVersion(version);
    }

}
