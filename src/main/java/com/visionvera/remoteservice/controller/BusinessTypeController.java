package com.visionvera.remoteservice.controller;

import com.visionvera.api.handler.constant.StorageApi;
import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.enums.IsLeafEnum;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.utils.StorageUploadHelper;
import com.visionvera.common.validator.group.AddBusinessDetailGroup;
import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.ChangeGroup;
import com.visionvera.common.validator.group.CheckGroup;
import com.visionvera.common.validator.group.DeleteGroup;
import com.visionvera.common.validator.group.UpdateBusinessDetailGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.common.validator.group.UpdateStateGroup;
import com.visionvera.common.validator.util.AssertUtil;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.pojo.BusinessTypeVo;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.service.BusinessTypeService;
import com.visionvera.remoteservice.util.FileUploadUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;

import java.io.File;
import java.util.Map;

import com.visionvera.remoteservice.util.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Auther: quboka
 * @Date: 2018/8/23 17:48
 * @Description: 业务类别
 */
@RestController
@RequestMapping("/businessType")
public class BusinessTypeController {

  private static Logger logger = LoggerFactory.getLogger(BusinessTypeController.class);

  @Autowired
  private BusinessTypeService businessTypeService;
  @Resource
  private StorageUploadHelper storageUploadHelper;
  @Value("${temp.save.path}")
  private String tempPath;

  /**
   * @param [describes, deptId, serviceKey]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 加添业务类别
   * @author quboka
   * @date 2018/8/24 11:19
   */
  @SysLogAnno("添加业务类别")
  @RequiresPermissions("business:type:add")
  @PostMapping("/addBusinessClasses")
  public Map<String, Object> addBusinessClasses(@RequestBody BusinessTypeBean businessTypeBean) {
    ValidateUtil.validate(businessTypeBean, AddGroup.class);
    return businessTypeService.addBusinessClasses(businessTypeBean);

  }

  /**
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改业务类别
   * @author quboka
   * @date 2018/8/24 11:28
   */
  @SysLogAnno("修改业务类别")
  @RequiresPermissions("business:type:edit")
  @PostMapping("/updateBusinessClasses")
  public Map<String, Object> updateBusinessClasses(@RequestBody BusinessTypeBean businessTypeBean) {
    ValidateUtil.validate(businessTypeBean, UpdateGroup.class);
    return businessTypeService.updateBusinessClasses(businessTypeBean);
  }

  /**
   * @param [pageNum, pageSize, describes, deptId, serviceKey, id]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 业务类别查询分页
   * @author quboka
   * @date 2018/8/27 11:22
   */
  @RequiresPermissions("business:type:query")
  @PostMapping("/getBusinessClasses")
  public Map<String, Object> getBusinessClasses(@RequestBody BusinessTypeVo businessTypeVo) {
    SysUserBean userBean = ShiroUtils.getUserEntity();
    //0代表所有部门
    if (userBean.getDeptId() != CommonConstant.SUPER_ADMIN_DEPTID_TYPE) {
      businessTypeVo.setDeptId(userBean.getDeptId());
    }
    return businessTypeService.getBusinessClasses(businessTypeVo);
  }

  /**
   * id查询业务详情
   *
   * @param businessDetailId 业务详情id
   * @return BusinessTypeBean 业务详情返回体
   */
  @RequiresPermissions("business:type:query")
  @GetMapping("/getBusinessDetailById")
  public Map<String, Object> getBusinessDetailById(@RequestParam("businessDetailId")Integer businessDetailId) {
    AssertUtil.isNull(businessDetailId,"业务详情id不能为空");
    return businessTypeService.getBusinessDetailById(businessDetailId);
  }

  /**
   * id查询业务类型
   *
   * @param businessTypeId 业务详情id
   * @return BusinessTypeBean 业务类型返回体
   */
  @RequiresPermissions("business:type:query")
  @GetMapping("/getBusinessTypeById")
  public Map<String, Object> getBusinessTypeById(@RequestParam("businessTypeId")Integer businessTypeId) {
    AssertUtil.isNull(businessTypeId,"业务类型id不能为空");
    return businessTypeService.getBusinessTypeById(businessTypeId);
  }


  /**
   * @param [pageNum, pageSize, describes, deptId, serviceKey, id]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 业务类别列表查询
   * @author ljfan
   * @date 2018/11/14 11:22
   */
  @GetMapping("getBusinessTypeInfoById")
  public Map<String, Object> getBusinessTypeInfoById(Integer id) {
    AssertUtil.isEmpty(id, "id不能为空");
    return businessTypeService.getBusinessTypeInfoById(id);
  }

  /**
   * @param [pageNum, pageSize, describes, deptId, serviceKey, id]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 业务类别列表查询
   * @author ljfan
   * @date 2018/11/14 11:22
   */
  @PostMapping("/getBusinessClassesNoPage")
  public Map<String, Object> getBusinessClassesNoPage(@RequestBody BusinessTypeVo businessTypeVo) {
    ValidateUtil.validate(businessTypeVo, CheckGroup.class);
    BusinessTypeBean businessTypeBean = new BusinessTypeBean();
    businessTypeBean.setDeptId(businessTypeVo.getDeptId());
    businessTypeBean.setServiceKey(businessTypeVo.getServiceKey());
    businessTypeBean.setIsLeaf(IsLeafEnum.No.getValue());
    businessTypeBean.setState(StateEnum.Effective.getValue());
    return businessTypeService.getBusinessClassesNoPage(businessTypeBean);
  }

  /**
   * @param [businessTypeBean]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 添加业务详情
   * @author quboka
   * @date 2018/8/27 15:25
   */
  @SysLogAnno("添加业务详情")
  @PostMapping("addBusinessType")
  public Map<String, Object> addBusinessType(@RequestBody BusinessTypeVo businessTypeVo) {
    ValidateUtil.validate(businessTypeVo, AddBusinessDetailGroup.class);
    return businessTypeService.addBusinessType(businessTypeVo);
  }

  /**
   * @param [businessTypeBean]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改业务详情
   * @author quboka
   * @date 2018/8/27 15:25
   */
  @SysLogAnno("修改业务详情")
  @PostMapping("updateBusinessType")
  public Map<String, Object> updateBusinessType(@RequestBody BusinessTypeVo businessTypeVo) {
    ValidateUtil.validate(businessTypeVo, UpdateBusinessDetailGroup.class);
    return businessTypeService.updateBusinessType(businessTypeVo);
  }

  /**
   * 根据父id获取所有业务类型 不传父id 为全部
   *
   * @param parentId 父id
   * @return
   */
/*  @GetMapping("getBusinessType")
  public Map<String, Object> getBusinessType(
      @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
    return businessTypeService.getBusinessType(parentId);
  }*/

  /**
   * @param [status, id, version]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改状态
   * @author quboka
   * @date 2018/8/30 17:48
   */
  @SysLogAnno("修改状态")
  @RequiresPermissions("business:type:changeStatus")
  @PostMapping("updateState")
  public Map<String, Object> updateState(@RequestBody BusinessTypeBean businessTypeBean) {
    ValidateUtil.validate(businessTypeBean, ChangeGroup.class);
    return businessTypeService.updateState(businessTypeBean);
  }

  /**
   * @param [ids]
   * @param isDel 1：删除归属业务  0：移到其他类别中
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除类别
   * @author quboka
   * @date 2018/8/24 17:28
   */
  @SysLogAnno("删除类别")
  @RequiresPermissions("business:type:delete")
  @PostMapping("/deleteBusinessClasses")
  public Map<String, Object> deleteBusinessClasses(@RequestBody BusinessTypeVo businessTypeVo) {
    ValidateUtil.validate(businessTypeVo, DeleteGroup.class);
    Integer[] ids = businessTypeVo.getIds();
    Integer isDel = businessTypeVo.getIsDel();
    return businessTypeService.deleteBusinessClasses(ids, isDel);
  }

  /**
   * @param [ids]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除业务详情
   * @author quboka
   * @date 2018/8/31 10:54
   */
  @SysLogAnno("删除业务详情")
  @GetMapping("/deleteBusinessType")
  public Map<String, Object> deleteBusinessType(Integer[] ids) {
    AssertUtil.isEmpty(ids, "删除业务详情失败,请勾选");
    return businessTypeService.deleteBusinessType(ids);
  }

  /**
   * 校验业务详情
   *
   * @param businessTypeBean
   * @return
   */
  @PostMapping("/checkoutType")
  public Map<String, Object> checkoutType(@RequestBody BusinessTypeBean businessTypeBean) {
    return businessTypeService.checkoutType(businessTypeBean);
  }

  /**
   * 　　* @Description: 启用/禁用服务类别 　　* @author: xueshiqi 　　* @date: 2018/10/29
   */
  @SysLogAnno("更改服务状态")
  @PostMapping("/updateBusinessTypeState")
  public Map<String, Object> updateBusinessTypeState(
      @RequestBody BusinessTypeVo businessTypeVo) {
    ValidateUtil.validate(businessTypeVo, UpdateStateGroup.class);
    return businessTypeService.updateBusinessTypeState(businessTypeVo);
  }

  /**
   * 新建业务时上传表单接口,前端要求走接口，实际可不上传
   * @param file
   * @return
   */
  @PostMapping("/uploadForm")
  public Map<String, Object> uploadForm(MultipartFile file){
      return ResultUtils.ok("上传表单成功");
  }

}
