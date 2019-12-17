package com.visionvera.remoteservice.controller;

import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.QueryGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.common.validator.util.AssertUtil;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.pojo.SysDeptVo;
import com.visionvera.remoteservice.service.SysDeptService;
import com.visionvera.remoteservice.util.ShiroUtils;
import com.visionvera.remoteservice.util.StringUtil;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ljfan on 2018/10/29. 部门
 */
@RestController
@RequestMapping("/sysDept")
public class SysDeptController {

  Logger logger = LogManager.getLogger(getClass());

  @Resource
  private SysDeptService sysDeptService;
  /**
   * 添加部门
   *
   * @param deptName
   * @return
   * @auther ljfan
   */
  @SysLogAnno("添加部门")
  @RequiresPermissions("dept:add")
  @RequestMapping("/addSysDept")
  public Map<String, Object> addSysDept(@RequestParam("deptName") String deptName, @RequestParam("state") Integer state,@RequestParam("type") Integer type, MultipartFile stampFile) {
    SysDeptBean sysDeptBean = new SysDeptBean();
    sysDeptBean.setDeptName(deptName);
    sysDeptBean.setState(state);
    sysDeptBean.setType(type);
    ValidateUtil.validate(sysDeptBean, AddGroup.class);
    return sysDeptService.addSysDept(sysDeptBean,stampFile);
  }

  /**
   * 修改部门
   *
   * @param deptName
   * @return
   */
  @SysLogAnno("修改部门")
  @RequiresPermissions("dept:edit")
  @RequestMapping("/updateSysDept")
  public Map<String, Object> updateDept(@RequestParam("deptName") String deptName, @RequestParam("state") Integer state, @RequestParam("type") Integer type,MultipartFile stampFile, @RequestParam("id") Integer id, @RequestParam(value = "stampUrl", required = false) String stampUrl, @RequestParam(value = "stampName", required = false) String stampName) {
    SysDeptBean sysDeptBean = new SysDeptBean();
    sysDeptBean.setDeptName(deptName);
    sysDeptBean.setState(state);
    sysDeptBean.setId(id);
    if(StringUtil.isNotEmpty(stampUrl)){
    sysDeptBean.setStampUrl(stampUrl);
    }
    sysDeptBean.setType(type);
    sysDeptBean.setStampName(stampName);
    ValidateUtil.validate(sysDeptBean, UpdateGroup.class);
    return sysDeptService.updateSysDept(sysDeptBean, stampFile);
  }

  /**
   * 查询部门
   *
   * @param sysDeptVo
   * @return
   */
  @RequiresPermissions("dept:query")
  @PostMapping("/getSysDeptList")
  public Map<String, Object> getDeptList(@RequestBody SysDeptVo sysDeptVo) {
    ValidateUtil.validate(sysDeptVo, QueryGroup.class);
    return sysDeptService.getSysDeptList(sysDeptVo);
  }
  /**
   * 查询部门
   *
   * @param id
   * @return
   */
  @RequiresPermissions("dept:query")
  @RequestMapping("/getSysDeptById")
  public Map<String, Object> getSysDeptById(Integer deptId) {
    AssertUtil.isNull(deptId,"部门id不能为空");
    return sysDeptService.getSysDeptById(deptId);
  }

  /**
   * 查询部门  无分页，当为系统管理员时，查询出所以部门，否则查询出当前用户隶属部门
   *
   * @param id
   * @return
   */
  @RequestMapping(value = "/getSysDeptListNoPage", method = RequestMethod.POST)
  public Map<String, Object> getSysDeptListNoPage(Integer id) {
    SysUserBean sysUser = ShiroUtils.getUserEntity();
    if ((CommonConstant.SUPER_ADMIN.equals(sysUser.getType())) || (sysUser.getDeptId()
        == CommonConstant.SUPER_ADMIN_DEPTID_TYPE)) {
     return sysDeptService.getSysDeptListNoPage(null);
    } else {
      return sysDeptService.getSysDeptListNoPage(sysUser.getDeptId());
    }
  }

  /**
   * 查询部门，查询非删除状态，供系统设置页面使用
   * @return
   */
  @GetMapping(value = "/getSysDeptListNotInDelete")
  public Map<String, Object> getSysDeptListNotInDelete(){
    return sysDeptService.getSysDeptListNotInDelete();
  }

  /**
   * @param [deptStatus, id, version]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改状态
   * @author ljfan
   * @date 2018/10/30 17:38
   */
  @SysLogAnno("修改状态")
  @RequiresPermissions("dept:changeStatus")
  @RequestMapping("/updateState")
  public Map<String, Object> updateState(@RequestBody SysDeptBean sysDeptBean) {
    return sysDeptService.updateState(sysDeptBean);
  }


  /**
   * 删除
   *
   * @param ids
   * @return
   */
  @SysLogAnno("删除部门")
  @RequiresPermissions("dept:delete")
  @RequestMapping("/deleteSysDept")
  public Map<String, Object> deleteDept(
      @RequestParam(value = "id", required = true) Integer[] id) {
    AssertUtil.isEmpty(id, "删除部门失败，缺少参数");
    return sysDeptService.deleteSysDept(id);
  }

  /**
   * 获取部门章路径
   * @param id 部门id
   * @return
   */
  @RequestMapping("/getDeptStamp")
  public Map<String, Object> getDeptStamp(Integer id){
    AssertUtil.isEmpty(id, "获取失败，请传部门id");
    return sysDeptService.getDeptStamp(id);
  }

  /**
   * 根据业务id查询对应部门部门章
   * @param businessId
   * @return
   */
  @RequestMapping("/getDeptStampByBusinessId")
  public Map<String, Object> getDeptStampByBusinessId(Integer businessId){
    return sysDeptService.getDeptStampByBusinessId(businessId);
  }

  @RequestMapping("/getDeptByDeptName")
  public Map<String, Object> getDeptByDeptName(String deptName) {
    return sysDeptService.getDeptByDeptName(deptName);
  }

  /**
   * 受理端业务变更时的部门列表
   * @return
   */
  @GetMapping("deptListForChangeBusiness.do")
  public Map<String, Object>deptListForChangeBusiness(){
    return sysDeptService.deptListForChangeBusiness();
  }
}
